package com.argusoft.sewa.android.app.component;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.argusoft.sewa.android.app.R;
import com.argusoft.sewa.android.app.core.NcdService;
import com.argusoft.sewa.android.app.databean.MedicineListItemDataBean;
import com.argusoft.sewa.android.app.databean.NcdMemberMedicineDataBean;
import com.argusoft.sewa.android.app.datastructure.QueFormBean;
import com.argusoft.sewa.android.app.model.DrugInventoryBean;
import com.argusoft.sewa.android.app.util.SewaUtil;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;

public class MedicineListAdapter extends RecyclerView.Adapter<MedicineListAdapter.ViewHolder> {

    private final Context context;
    private final List<MedicineListItemDataBean> list;
    private final Map<Integer, View> adapterView;
    private final Boolean isAdditional;
    private final QueFormBean queFormBean;
    private final NcdService ncdService;
    private final Map<Integer, NcdMemberMedicineDataBean> prescribedMedicineMap;
    private List<MedicineListItemDataBean> medicineDetailsList = new ArrayList<>();

    public MedicineListAdapter(Context context, List<MedicineListItemDataBean> list, Boolean isAdditional, QueFormBean queFormBean, NcdService ncdService, Map<Integer, NcdMemberMedicineDataBean> prescribedMedicineMap) {
        this.context = context;
        this.list = list;
        this.isAdditional = isAdditional;
        this.queFormBean = queFormBean;
        this.ncdService = ncdService;
        this.prescribedMedicineMap = new HashMap<>();
        if (prescribedMedicineMap != null && !prescribedMedicineMap.isEmpty()) {
            this.prescribedMedicineMap.putAll(prescribedMedicineMap);
        }
        this.adapterView = new HashMap<>();
    }

    public void setItems(List<MedicineListItemDataBean> list) {
        this.list.clear();
        this.adapterView.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.listview_row_medicine_detail, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        MedicineListItemDataBean itemDataBean = list.get(position);
        if (itemDataBean.getExpiryDate() != null &&
                itemDataBean.getExpiryDate().before(new Date())) {
            holder.mainLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.expired_medicine_background));
        }
        holder.title.setText(String.format("Medicine Name : %s", itemDataBean.getMedicineName()));
        if (!isAdditional) {
            NcdMemberMedicineDataBean bean = prescribedMedicineMap.get(itemDataBean.getMedicineId());
            if (bean != null) {
                holder.title.setText(String.format("Medicine Name : %s (%s/%s/%s)", itemDataBean.getMedicineName(), bean.getFrequency(), bean.getDuration(), bean.getQuantity()));
            }
        }

        if (itemDataBean.getStock() != null) {
            holder.stockVal.setText(String.valueOf(itemDataBean.getStock()));
        }
        holder.stockVal.setEnabled(false);
        if (!isAdditional) {
            holder.frequencyVal.setText(String.valueOf(itemDataBean.getFrequency()));
            holder.frequencyVal.setEnabled(false);
            if (itemDataBean.getSpecialInstruction() != null) {
                holder.specialInstructionVal.setText(String.valueOf(itemDataBean.getSpecialInstruction()));
            }
            if (itemDataBean.getExpiryDate() != null &&
            itemDataBean.getExpiryDate().before(new Date())) {
                holder.durationVal.getText().clear();
                holder.quantityVal.getText().clear();
            } else {
                holder.durationVal.setText(String.valueOf(itemDataBean.getDuration()));
                holder.quantityVal.setText(String.valueOf(itemDataBean.getQuantity()));
            }
        } else {
            holder.frequencyVal.setEnabled(true);
        }

        holder.frequencyVal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                onChangeFrequency(itemDataBean, charSequence, holder);
                setAnswer(itemDataBean);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        holder.durationVal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                onChangeDuration(itemDataBean, charSequence, holder);
                setAnswer(itemDataBean);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        holder.specialInstructionVal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() != 0) {
                    itemDataBean.setSpecialInstruction(charSequence.toString());
                }
                setAnswer(itemDataBean);
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        if (list != null) {
            return list.size();
        }
        return 0;
    }

    private void setAnswer(MedicineListItemDataBean itemDataBean) {
        if (queFormBean != null) {
            if (isAdditional && itemDataBean.getFrequency() == null && itemDataBean.getDuration() == null) {
                if (medicineDetailsList.contains(itemDataBean)) {
                    medicineDetailsList.remove(itemDataBean);
                }
            } else {
                if (!medicineDetailsList.contains(itemDataBean)) {
                    NcdMemberMedicineDataBean dataBean = prescribedMedicineMap.get(itemDataBean.getMedicineId());
                    medicineDetailsList.add(itemDataBean);
                    if (dataBean != null && !isAdditional && dataBean.getDuration().equals(itemDataBean.getDuration()) &&
                            dataBean.getFrequency().equals(itemDataBean.getFrequency())) {
                        medicineDetailsList.remove(itemDataBean);
                    }
                }
            }
            queFormBean.setAnswer(new Gson().toJson(medicineDetailsList));
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        LinearLayout mainLayout;
        TextView title;
        EditText frequencyVal;
        EditText durationVal;
        EditText quantityVal;
        EditText stockVal;
        EditText specialInstructionVal;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mainLayout = itemView.findViewById(R.id.main_layout);
            title = itemView.findViewById(R.id.medicine_name);
            frequencyVal = itemView.findViewById(R.id.frequency_value);
            durationVal = itemView.findViewById(R.id.duration_value);
            quantityVal = itemView.findViewById(R.id.quantity_value);
            stockVal = itemView.findViewById(R.id.stock_value);
            specialInstructionVal = itemView.findViewById(R.id.special_instruction_value);
        }
    }

    private void onChangeDuration(MedicineListItemDataBean itemDataBean, CharSequence duration, ViewHolder holder) {
        if (itemDataBean != null) {
            DrugInventoryBean drugInventoryBean = ncdService.retrieveDrugByMedicineId(itemDataBean.getMedicineId());
            Integer availableStock = drugInventoryBean.getBalanceInHand();
            Integer originalQuantity = null;
            Integer originalFrequency = null;
            Integer originalDuration = null;
            if (!isAdditional) {
                NcdMemberMedicineDataBean medicineDetail = prescribedMedicineMap.get(itemDataBean.getMedicineId());
                if (medicineDetail != null) {
                    originalQuantity = medicineDetail.getQuantity();
                    originalFrequency = medicineDetail.getFrequency();
                    originalDuration = medicineDetail.getDuration();
                }
            }

            if (duration.length() != 0) {
                itemDataBean.setDuration(Integer.valueOf(duration.toString()));
                String frequency = holder.frequencyVal.getText().toString();
                if (!frequency.isEmpty()) {
                    int stockCalc;
                    int quantityCalc = Integer.parseInt(frequency) * Integer.parseInt(duration.toString());
                    if (originalQuantity == null) {
                        stockCalc = availableStock - quantityCalc;
                    } else if (originalQuantity < quantityCalc) {
                        stockCalc = availableStock - (quantityCalc - originalQuantity);
                    } else if (originalQuantity > quantityCalc) {
                        stockCalc = availableStock + (originalQuantity - quantityCalc);
                    } else {
                        stockCalc = availableStock;
                    }
                    if (stockCalc >= 0) {
                        holder.quantityVal.setText(String.valueOf(quantityCalc));
                        holder.stockVal.setText(String.valueOf(stockCalc));
                        itemDataBean.setStock(stockCalc);
                        itemDataBean.setQuantity(quantityCalc);
                    } else {
                        SewaUtil.generateToast(context, "Stock not available");
                        holder.durationVal.getText().clear();
                        itemDataBean.setDuration(null);
                        holder.quantityVal.getText().clear();
                    }
                }
            } else {
                itemDataBean.setDuration(null);
                holder.quantityVal.getText().clear();
                holder.stockVal.setText(String.valueOf(availableStock));
            }
        }
    }

    private void onChangeFrequency(MedicineListItemDataBean itemDataBean, CharSequence frequency, ViewHolder holder) {
        if (itemDataBean != null) {
            DrugInventoryBean drugInventoryBean = ncdService.retrieveDrugByMedicineId(itemDataBean.getMedicineId());
            Integer availableStock = drugInventoryBean.getBalanceInHand();
            if (frequency.length() != 0) {
                itemDataBean.setFrequency(Integer.valueOf(frequency.toString()));
                String duration = holder.durationVal.getText().toString();
                if (!duration.isEmpty()) {
                    int stockCalc;
                    int quantityCalc = Integer.parseInt(duration) * Integer.parseInt(frequency.toString());
                    stockCalc = availableStock - quantityCalc;
                    if (stockCalc >= 0) {
                        holder.quantityVal.setText(String.valueOf(quantityCalc));
                        holder.stockVal.setText(String.valueOf(stockCalc));
                        itemDataBean.setDuration(Integer.parseInt(duration));
                        itemDataBean.setStock(stockCalc);
                        itemDataBean.setQuantity(quantityCalc);
                    } else {
                        SewaUtil.generateToast(context, "Stock not available");
                        holder.frequencyVal.getText().clear();
                        itemDataBean.setFrequency(null);
                        holder.quantityVal.getText().clear();
                    }
                }
            } else {
                itemDataBean.setFrequency(null);
                holder.quantityVal.getText().clear();
                holder.stockVal.setText(String.valueOf(availableStock));
            }
        }
    }

}
