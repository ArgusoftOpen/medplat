/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.sewa.android.app.datastructure;

import android.content.Context;
import com.argusoft.sewa.android.app.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.argusoft.sewa.android.app.component.MyStaticComponents;
import com.argusoft.sewa.android.app.component.MyVaccination;
import com.argusoft.sewa.android.app.databean.OptionTagBean;
import com.argusoft.sewa.android.app.util.DynamicUtils;
import com.argusoft.sewa.android.app.util.GlobalTypes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * @author alpeshkyada
 */
public class PageFormBean implements Cloneable {

    public static Context context;
    private boolean isFirst; // for add into main body layout  or just display
    private int pageNo; // page no
    private int loopCounter; //loop counter used while if looped page occurs
    private List<Integer> listOfQuestion; // all question of page
    private List<Integer> listOfShowQuestion; //visible question of page
    private List<Integer> listOfNext;
    private LinearLayout pageLayout; // one final view of current page
    private PageFormBean nextPage; // next page in navigator
    private PageFormBean prePage; // previous page in navigator
    private String event;  // to check is last page or not to generate string submit or cancel
    private String validationMessage;
    private int lastQuestionId;// last question id
    private int firstQuestionId;
    private MyVaccination myVaccination;
    private boolean isVaccinations;

    /**
     * that will create new page with passing new page id
     */
    public PageFormBean(int pageNo) {
        this.pageNo = pageNo;
        this.loopCounter = SharedStructureData.loopBakCounter;
        this.listOfQuestion = null;
        this.listOfShowQuestion = null;
        this.listOfNext = null;
        this.pageLayout = null;
        this.nextPage = null;
        this.prePage = null;
        this.validationMessage = null;
        this.isFirst = true;
        this.event = null;
        this.myVaccination = null;
        this.isVaccinations = false;
    }

    /**
     * that will add question into treeView
     *
     * @return true if added else false
     */
    public MyVaccination getMyVaccination() {
        return myVaccination;
    }

    public void setMyVaccination(MyVaccination myVaccination) {
        this.myVaccination = myVaccination;
    }

    public boolean isIsVaccinations() {
        return isVaccinations;
    }

    public void setIsVaccinations(boolean isVaccinations) {
        this.isVaccinations = isVaccinations;
    }

    public String getValidationMessage() {
        return validationMessage;
    }

    public void setValidationMessage(String validationMessage) {
        this.validationMessage = validationMessage;
    }

    public int getFirstQuestionId() {
        return firstQuestionId;
    }

    public void setFirstQuestionId(int firstQuestionId) {
        this.firstQuestionId = firstQuestionId;
    }

    public boolean addQuestion(QueFormBean question) {
        if (question.getEvent() != null) {
            if (question.getEvent().trim().equalsIgnoreCase(GlobalTypes.EVENT_OKAY)) {
                this.event = GlobalTypes.EVENT_OKAY;
            } else if (question.getEvent().trim().equalsIgnoreCase(GlobalTypes.EVENT_SUBMIT)) {
                this.event = GlobalTypes.EVENT_SUBMIT;
            } else if (question.getEvent().trim().equalsIgnoreCase(GlobalTypes.EVENT_CANCEL)) {
                this.event = GlobalTypes.EVENT_CANCEL;
            } else if (question.getEvent().trim().equalsIgnoreCase(GlobalTypes.EVENT_SAVE_FORM)) {
                this.event = GlobalTypes.EVENT_SAVE_FORM;
            } else {
                this.event = GlobalTypes.EVENT_NEXT;
            }
        }
        List<Integer> nextQuestionIdList = DynamicUtils.getNextQuestionIdList(question);
        if (!nextQuestionIdList.isEmpty()) {
            if (listOfNext == null) {
                listOfNext = new ArrayList<>();
            }
            listOfNext.addAll(nextQuestionIdList);
        }
        int questionId = DynamicUtils.getLoopId(question.getId(), question.getLoopCounter());
        if (listOfQuestion == null) {
            listOfQuestion = new ArrayList<>();
        }
        return listOfQuestion.add(questionId);
    }

    /**
     * @return return the page id that store in map index referencing this page
     */
    public int getPageNo() {
        return DynamicUtils.getLoopId(pageNo, loopCounter);
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getLoopCounter() {
        return loopCounter;
    }

    public void setLoopCounter(int loopCounter) {
        this.loopCounter = loopCounter;
    }

    public int getLastQuestionId() {
        return lastQuestionId;
    }

    public void setLastQuestionId(int lastQuestionId) {
        this.lastQuestionId = lastQuestionId;
    }

    public boolean isIsFirst() {
        return isFirst;
    }

    public void setIsFirst(boolean isFirst) {
        this.isFirst = isFirst;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public List<Integer> getListOfQuestion() {
        return listOfQuestion;
    }

    public void setListOfQuestion(List<Integer> listOfQuestion) {
        this.listOfQuestion = listOfQuestion;
    }

    public List<Integer> getListOfShowQuestion() {
        return listOfShowQuestion;
    }

    public void setListOfShowQuestion(List<Integer> listOfShowQuestion) {
        this.listOfShowQuestion = listOfShowQuestion;
    }

    public void setPageLayout(LinearLayout pageLayout) {
        this.pageLayout = pageLayout;
    }

    /**
     * once you added all questions in page than call this to get view of this
     * page
     *
     * @return return page view
     */
    public LinearLayout getPageLayout(boolean getView, int bodyLayoutPadding) {
        int next = 0;
        int firstQues = 0;
        if (pageLayout == null || listOfShowQuestion == null || listOfShowQuestion.isEmpty()) {
            Log.i(PageFormBean.class.getSimpleName(), "first question is : " + firstQuestionId);
            if (getView || context == null) {
                return null;
            }

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT);
            pageLayout = MyStaticComponents.getLinearLayout(context, -1, LinearLayout.VERTICAL, layoutParams);
            if (listOfQuestion != null && listOfQuestion.size() > 1) {
                if (firstQuestionId <= 0) {
                    for (Integer questionId : listOfQuestion) {
                        if (listOfNext != null && !listOfNext.contains(questionId)) {
                            firstQuestionId = firstQues = questionId;
                            break;
                        }
                    }
                } else {
                    firstQues = firstQuestionId;
                }
                Log.i(PageFormBean.class.getSimpleName(), "first question is : " + firstQuestionId);

                boolean flag = false;
                LinearLayout hiddenQuestions = null;
                int counter = 0;
                do {
                    QueFormBean question = SharedStructureData.mapIndexQuestion.get(firstQues);
                    if (question != null) {
                        if (question.getIshidden().equalsIgnoreCase(GlobalTypes.FALSE)) {

                            if (listOfShowQuestion == null) {
                                listOfShowQuestion = new ArrayList<>();
                            }
                            if (flag) {
                                View view = (View) question.getQuestionUIFrame();
                                view.setVisibility(View.GONE);
                                hiddenQuestions.addView(view);
                                if (counter == 1) {
                                    counter++;
                                    hiddenQuestions.setVisibility(View.GONE);
                                    pageLayout.addView(hiddenQuestions);
                                }
                            } else {
                                if (question.getQuestionUIFrame() != null)
                                    pageLayout.addView((View) question.getQuestionUIFrame());
                                if (question.getType().equals(GlobalTypes.MEMBER_DETAILS_COMPONENT)) {
                                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT);
                                    bodyLayoutPadding = bodyLayoutPadding * (-1);
                                    params.setMargins(bodyLayoutPadding, bodyLayoutPadding, bodyLayoutPadding, 0);
                                    pageLayout.setLayoutParams(params);
                                }
                            }
                            listOfShowQuestion.add(firstQues);
                        } else {
                            DynamicUtils.applyFormulaForHiddenQuestion(question, false);
                            //appy formula to hidden
                        }
                        next = DynamicUtils.getNext(question);
                        if (next == -1) {
                            flag = true;
                            List<Integer> myNexts = DynamicUtils.getNextQuestionIdList(question);
                            if (counter == 0) {
                                counter++;
                                hiddenQuestions = MyStaticComponents.getLinearLayout(context, DynamicUtils.HIDDEN_LAYOUT_ID, LinearLayout.VERTICAL,
                                        new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT));
                            }
                            next = hideQuestions(myNexts, hiddenQuestions);
                        }
                        if (next != firstQues && listOfQuestion.contains(next)) {
                            firstQues = next;
                        } else {
                            break;
                        }
                    } else {
                        break;
                    }
                } while (true);
                if (counter == 1) {
                    hiddenQuestions.setVisibility(View.GONE);
                    pageLayout.addView(hiddenQuestions);
                }
            } else if (listOfQuestion != null && listOfQuestion.size() == 1) {
                int queId = listOfQuestion.get(0);
                firstQuestionId = queId;
                QueFormBean question = SharedStructureData.mapIndexQuestion.get(queId);
                if (question != null && question.getIshidden().equalsIgnoreCase(GlobalTypes.FALSE)) {
                    if (question.getType().equals(GlobalTypes.MEMBER_DETAILS_COMPONENT)) {
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT);
                        bodyLayoutPadding = bodyLayoutPadding * (-1);
                        params.setMargins(bodyLayoutPadding, bodyLayoutPadding, bodyLayoutPadding, 0);
                        pageLayout.setLayoutParams(params);
                    }
                    pageLayout.addView((View) question.getQuestionUIFrame());
                    listOfShowQuestion = new ArrayList<>();
                    listOfShowQuestion.add(queId);
                } else if (question != null && question.getNext() != null && question.getNext().trim().length() > 0) {
                    DynamicUtils.applyFormulaForHiddenQuestion(question, false);
                    next = DynamicUtils.getLoopId(Integer.parseInt(question.getNext()), question.getLoopCounter());
                }
            }
            isFirst = false;
            if (listOfShowQuestion == null) {
                FormEngine.setNext(next);
                return null;
            }
        }
        return pageLayout;
    }

    /**
     * that will return dynamic next page id Automatic
     *
     * @return next page id or -1 to not found 0 for verify all pages
     */
    public int getNextPageId() {
        if (listOfShowQuestion != null && !listOfShowQuestion.isEmpty()) {
            QueFormBean question = SharedStructureData.mapIndexQuestion.get(lastQuestionId);
            if (question != null && question.getNext() != null) {
                return getNextLoopPageId(question);
            } else {
                return -1;
            }
        } else {
            return -1;
        }
    }

    public PageFormBean getNextPage() {
        return nextPage;
    }

    public void setNextPage(PageFormBean nextPage) {
        this.nextPage = nextPage;
        nextPage.setPrePage(this);
    }

    public PageFormBean getPrePage() {
        return prePage;
    }

    public void setPrePage(PageFormBean prePage) {
        this.prePage = prePage;
    }

    @NonNull
    @Override
    public String toString() {
        return "PageFormBean{" + "pageNo=" + pageNo + ", loopCounter=" + loopCounter + ", listOfQuestion=" + listOfQuestion + "\nlistOfShowQuestion=" + listOfShowQuestion + "\nlistOfNext=" + listOfNext + "}\n";
    }

    /**
     * that will return next page id if any loop occurs than clone that page and
     * gives new cloned page id
     */
    private int getNextLoopPageId(QueFormBean question) {
        if (question.getEvent() != null && (question.getEvent().trim().equalsIgnoreCase(GlobalTypes.EVENT_LOOP))) {
            List<OptionTagBean> options = question.getOptions();
            if (question.getAnswer() != null && options != null && !options.isEmpty()) {
                if (question.getAnswer().toString().equalsIgnoreCase(options.get(0).getKey())) {
                    int nextQueId = DynamicUtils.getLoopId(Integer.parseInt(question.getNext().trim()), question.getLoopCounter() + 1);
                    QueFormBean nextQuestion = SharedStructureData.mapIndexQuestion.get(nextQueId);
                    if (nextQuestion == null) {
                        // do clone the page 
                        SharedStructureData.loopBakCounter = question.getLoopCounter() + 1;
                        question = getNextVisibleQues(nextQueId, question.getLoopCounter() + 1, true, question.isIgnoreLoop(), question.isIgnoreNextQueLoop());

                        if (question != null && question.getPage() != null) {
                            DynamicUtils.setFirstQuestion(question);
                            return DynamicUtils.getLoopId(Integer.parseInt(question.getPage().trim()), question.getLoopCounter());
                        } else {
                            return -1;
                        }
                    } else {
                        //return the next page
                        if (nextQuestion.getPage() != null) {
                            DynamicUtils.setFirstQuestion(nextQuestion);
                            return DynamicUtils.getLoopId(Integer.parseInt(nextQuestion.getPage().trim()), nextQuestion.getLoopCounter());
                        } else {
                            return -1;
                        }
                    }
                } else {
                    SharedStructureData.loopBakCounter = question.getLoopCounter();
                    question = getNextVisibleQues(Integer.parseInt(question.getNext().trim()), 0, false, question.isIgnoreLoop(), question.isIgnoreNextQueLoop());
                    if (question != null && question.getPage() != null) {
                        DynamicUtils.setFirstQuestion(question);
                        return Integer.parseInt(question.getPage().trim());
                    } else {
                        return -1;
                    }
                }
            } else {
                return -1;
            }
        } else {
            int loopQue = question.getLoopCounter();
            int nextQue = DynamicUtils.getLoopId(Integer.parseInt(question.getNext()), loopQue);
            question = getNextVisibleQues(nextQue, loopQue, false, question.isIgnoreLoop(), question.isIgnoreNextQueLoop());
            if (question != null && question.getPage() != null) {
                DynamicUtils.setFirstQuestion(question);
                return DynamicUtils.getLoopId(Integer.parseInt(question.getPage()), question.getLoopCounter());
            } else {
                if (nextQue == 0) {
                    return 0;
                } else {
                    return -1;
                }
            }
        }
    }

    /**
     * that will clone the page with given page id
     */
    private void clonePageById(int clonePageId, boolean ignoreProperty, boolean nextIgnore) {
        Log.i(PageFormBean.class.getSimpleName(), "Cloning start of Page id : " + clonePageId);

        PageFormBean pageFormBean = SharedStructureData.mapIndexPage.get(clonePageId);
        if (pageFormBean != null) {
            List<Integer> listQuestion = pageFormBean.getListOfQuestion();
            pageFormBean = new PageFormBean(clonePageId);
            SharedStructureData.mapIndexPage.put(pageFormBean.getPageNo(), pageFormBean);

            for (Integer que : listQuestion) {
                QueFormBean queFormBean = SharedStructureData.mapIndexQuestion.get(que);
                if (queFormBean != null) {
                    queFormBean = queFormBean.clone(ignoreProperty, nextIgnore, context);
                    pageFormBean.addQuestion(queFormBean);
                }
            }
            Log.i(PageFormBean.class.getSimpleName(), "Cloning stop of Page id : " + clonePageId + " with  new id : " + pageFormBean.getPageNo());

            pageFormBean.getPageNo();
        }
    }

    private QueFormBean getNextVisibleQues(int next, int loopCounter, boolean isClone, boolean ignoreProperty, boolean ignoreNextQuePrperty) {
        QueFormBean queFormBean = SharedStructureData.mapIndexQuestion.get(next);

        if (queFormBean != null) {
            if (queFormBean.getIshidden().equalsIgnoreCase(GlobalTypes.TRUE)) {
                Log.i(PageFormBean.class.getSimpleName(), next + " question is found and it is hidden :");
                // if no next is set for hidden question
                if (isClone) {
                    SharedStructureData.loopBakCounter = loopCounter;
                    queFormBean = queFormBean.clone(ignoreProperty, ignoreNextQuePrperty, context); // differnt when it come from default loop other wise same
                    int pageId = Integer.parseInt(queFormBean.getPage());
                    PageFormBean tmpPageFormBean = SharedStructureData.mapIndexPage.get(pageId);
                    if (tmpPageFormBean != null) {
                        tmpPageFormBean.addQuestion(queFormBean);
                    }

                    if (queFormBean.getNext() != null && queFormBean.getNext().trim().length() > 0) {
                        next = DynamicUtils.getLoopId(Integer.parseInt(queFormBean.getNext()), loopCounter);

                        if (queFormBean.getEvent() != null && queFormBean.getEvent().trim().equalsIgnoreCase(GlobalTypes.EVENT_DEFAULT_LOOP)) {
                            Log.i(PageFormBean.class.getSimpleName(), next + "=Event is default loop..............");
                            if (queFormBean.getAnswer() != null && queFormBean.getAnswer().toString().trim().toUpperCase().matches("[TY]")) {
                                next = DynamicUtils.getLoopId(Integer.parseInt(queFormBean.getNext()), loopCounter + 1);
                                return getNextVisibleQues(next, loopCounter + 1,
                                        SharedStructureData.mapIndexQuestion.get(next) == null,
                                        queFormBean.isIgnoreNextQueLoop(), queFormBean.isIgnoreLoop());
                            } else {
                                return getNextVisibleQues(Integer.parseInt(queFormBean.getNext()), 0, false, false, false);
                            }
                        } else {
                            return getNextVisibleQues(next, loopCounter, true, queFormBean.isIgnoreNextQueLoop(), queFormBean.isIgnoreNextQueLoop()); // 
                        }
                    } else {
                        Log.i(PageFormBean.class.getSimpleName(), next + "= is Not found for hidden question : " + queFormBean.getId());
                    }
                } else {
                    DynamicUtils.applyFormulaForHiddenQuestion(queFormBean, false);
                    if (queFormBean.getNext() != null && queFormBean.getNext().trim().length() > 0) {
                        next = DynamicUtils.getLoopId(Integer.parseInt(queFormBean.getNext()), loopCounter);
                        if (queFormBean.getEvent() != null && queFormBean.getEvent().trim().equalsIgnoreCase(GlobalTypes.EVENT_DEFAULT_LOOP)) {
                            Log.i(PageFormBean.class.getSimpleName(), next + "=Event is default loop..............");
                            if (queFormBean.getAnswer() != null && queFormBean.getAnswer().toString().trim().toUpperCase().matches("[TY]")) {
                                next = DynamicUtils.getLoopId(Integer.parseInt(queFormBean.getNext()), loopCounter + 1);
                                return getNextVisibleQues(next, loopCounter + 1,
                                        SharedStructureData.mapIndexQuestion.get(next) == null,
                                        queFormBean.isIgnoreNextQueLoop(), queFormBean.isIgnoreLoop());

                            } else {
                                return getNextVisibleQues(Integer.parseInt(queFormBean.getNext()), 0, false, false, false);
                            }
                        } else {
                            return getNextVisibleQues(next, loopCounter, false, queFormBean.isIgnoreNextQueLoop(), queFormBean.isIgnoreNextQueLoop());
                        }
                    } else {
                        Log.i(PageFormBean.class.getSimpleName(), next + "=Next is Not found for  question : " + queFormBean.getId());
                    }
                }
                return null; // if no next is set for hidden question
            } else {
                if (isClone) {
                    int pageId = Integer.parseInt(queFormBean.getPage());
                    SharedStructureData.loopBakCounter = loopCounter;
                    clonePageById(pageId, ignoreProperty, ignoreNextQuePrperty);
                    queFormBean = SharedStructureData.mapIndexQuestion.get(DynamicUtils.getLoopId(queFormBean.getId(), loopCounter));
                }
                return queFormBean;
            }
        } else {
            Log.i(PageFormBean.class.getSimpleName(), next + "= question is not found");
            if (next > 0) {
                int parentQuestion = DynamicUtils.getOriginalId(next, loopCounter);
                if (parentQuestion != next) {
                    return getNextVisibleQues(parentQuestion, loopCounter, true, ignoreProperty, ignoreNextQuePrperty);
                } else {
                    return null;
                }
            }
            return null;
        }
    }

    private int hideQuestions(List<Integer> myNexts, LinearLayout hiddenLayout) {
        List<Integer> nexts = new ArrayList<>();
        for (Integer i : myNexts) {
            if (listOfQuestion.contains(i)) {
                nexts.add(i);
            }
        }
        if (!nexts.isEmpty()) {
            Collections.sort(nexts);
            int nextoffirst = -1;
            for (int i = 0; i < nexts.size(); i++) {
                QueFormBean queFormBean = SharedStructureData.mapIndexQuestion.get(nexts.get(i));
                if (queFormBean != null) {
                    String next = queFormBean.getNext();
                    int tempNext = DynamicUtils.getLoopId(Integer.parseInt(next == null ? "-1" : next), queFormBean.getLoopCounter());
                    if (i == 0 || nextoffirst <= 0 || nextoffirst == nexts.get(i)) {
                        nextoffirst = tempNext;
                    }

                    if (queFormBean.getIshidden().equalsIgnoreCase(GlobalTypes.FALSE)) {
                        View view = (View) queFormBean.getQuestionUIFrame();
                        view.setVisibility(View.GONE);
                        hiddenLayout.removeView(view);
                        hiddenLayout.addView(view);
                        if (listOfShowQuestion == null) {
                            listOfShowQuestion = new ArrayList<>();
                        }
                        listOfShowQuestion.add(nexts.get(i));
                    }
                }
            }
            return nextoffirst;//return last
        } else {
            return -1;
        }
    }
}
