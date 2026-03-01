-- Add Hindi (HI) language labels to internationalization_label_master
-- This migration seeds Hindi translations for the WEB application

INSERT INTO internationalization_label_master (country, "key", "language", created_by, created_on, custom3b, "text", translation_pending, app_name)
VALUES
('IN', 'LOGIN.TITLE', 'HI', -1, now(), false, 'लॉगिन', false, 'WEB'),
('IN', 'LOGIN.USERNAME_PLACEHOLDER', 'HI', -1, now(), false, 'उपयोगकर्ता नाम', false, 'WEB'),
('IN', 'LOGIN.PASSWORD_PLACEHOLDER', 'HI', -1, now(), false, 'पासवर्ड', false, 'WEB'),
('IN', 'LOGIN.USERNAME_REQUIRED', 'HI', -1, now(), false, '*उपयोगकर्ता नाम आवश्यक है', false, 'WEB'),
('IN', 'LOGIN.PASSWORD_REQUIRED', 'HI', -1, now(), false, '*पासवर्ड आवश्यक है', false, 'WEB'),
('IN', 'LOGIN.SUBMIT', 'HI', -1, now(), false, 'लॉगिन', false, 'WEB'),
('IN', 'LOGIN.FORGOT_PASSWORD', 'HI', -1, now(), false, 'पासवर्ड भूल गए?', false, 'WEB'),
('IN', 'NAV.SEARCH_MENU', 'HI', -1, now(), false, 'मेनू खोजें', false, 'WEB'),
('IN', 'NAV.NO_DATA_FOUND', 'HI', -1, now(), false, 'कोई डेटा नहीं मिला', false, 'WEB'),
('IN', 'NAV.ACTIONS', 'HI', -1, now(), false, 'कार्रवाई', false, 'WEB'),
('IN', 'NAV.UPDATE_PROFILE', 'HI', -1, now(), false, 'प्रोफ़ाइल अपडेट करें', false, 'WEB'),
('IN', 'NAV.CHANGE_PASSWORD', 'HI', -1, now(), false, 'पासवर्ड बदलें', false, 'WEB'),
('IN', 'NAV.LOGOUT', 'HI', -1, now(), false, 'लॉगआउट', false, 'WEB'),
('IN', 'COMMON.SAVE', 'HI', -1, now(), false, 'सहेजें', false, 'WEB'),
('IN', 'COMMON.CANCEL', 'HI', -1, now(), false, 'रद्द करें', false, 'WEB'),
('IN', 'COMMON.ADD', 'HI', -1, now(), false, 'जोड़ें', false, 'WEB'),
('IN', 'COMMON.SEARCH', 'HI', -1, now(), false, 'खोजें', false, 'WEB'),
('IN', 'COMMON.CLOSE', 'HI', -1, now(), false, 'बंद करें', false, 'WEB'),
('IN', 'COMMON.DELETE', 'HI', -1, now(), false, 'हटाएं', false, 'WEB'),
('IN', 'COMMON.EDIT', 'HI', -1, now(), false, 'संपादित करें', false, 'WEB'),
('IN', 'COMMON.SUBMIT', 'HI', -1, now(), false, 'जमा करें', false, 'WEB'),
('IN', 'TOAST.PASSWORD_RESET_SUCCESS', 'HI', -1, now(), false, 'पासवर्ड रीसेट सफल!', false, 'WEB'),
('IN', 'TOAST.PASSWORD_RESET_ERROR', 'HI', -1, now(), false, 'कुछ गलत हो गया! कृपया पुनः प्रयास करें।', false, 'WEB'),
('IN', 'TOAST.PROFILE_UPDATED', 'HI', -1, now(), false, 'आपकी प्रोफ़ाइल सफलतापूर्वक अपडेट हो गई!', false, 'WEB'),
('IN', 'TOAST.DATA_UPDATED', 'HI', -1, now(), false, 'डेटा सफलतापूर्वक अपडेट किया गया', false, 'WEB')
ON CONFLICT DO NOTHING;
