(ns newsface.templates.accesstoken
  (:use [hiccup core page-helpers form-helpers]))


(defn accesstoken-page []
  (html5
    [:head
      [:title "Newsface - Suggesting you news based on your Facebook friends"]
      (include-css "/css/style.css")]
    [:body
     [:div.header
      [:a {:href "/"}
       [:img {:src "/imgs/logo.png"}]]]
     [:div.menu
      [:a {:href "/"} "Home"]
      [:a {:href "/accesstoken/"} "Access Token"]
      [:a {:href "/suggest/"} "Try it!"]
      [:a {:href "/news/"} "My News"]
      [:a {:href "/contacts/"} "Contacts"]]
     [:div.content
      [:h2 "What is an Access Token?"
       [:p "An access token is an alphanumerical code used by "
	"Facebook in order to authenticate web requests to "
	"its functionality."]]
      [:h2 "How can I get one, and why this can't be automatic?"
       [:p "Go to "
	[:a {:href "http://developers.facebook.com/docs/reference/api/"}
	 "Facebook Graph Api"] " and get it." [:br]
	"Extract a valid access token in a non-invasive way may be "
	"cumbersome, so the easiest way is to manually insert it. "
	"Don't forget that Newsface is still in beta: is not impossible "
	"that in future this feature will be turn automatic."]
       "My access token is: "]
      (form-to [:post "/settoken/"] 
      (text-field {:size 125} :access-token) 
      (submit-button "Set Access Token"))]
     [:div.footer
      [:div.footer-wrap
       [:div.contact
	[:strong "Contact"] [:br]
	"Support: " [:a {:href "https://github.com/CharlesStain/newsface"}
		     "Project page"]
	[:br]
	"Feedback: " [:a {:href "mailto:alfredo.dinapoli@gmail.com"}
		      "alfredo.dinapoli@gmail.com"]]]]]))


(defn token-response
  [token]
  (html5
    [:head
      [:title "Newsface - Suggesting you news based on your Facebook friends"]
      (include-css "/css/style.css")]
    [:body
     [:div.header
      [:a {:href "/"}
       [:img {:src "/imgs/logo.png"}]]]
     [:div.menu
      [:a {:href "/"} "Home"]
      [:a {:href "/accesstoken/"} "Access Token"]
      [:a {:href "/suggest/"} "Try it!"]
      [:a {:href "/news/"} "My News"]
      [:a {:href "/contacts/"} "Contacts"]]
     [:div.content
      [:h2 "Operation Completed"
       [:p "Token " [:b token] " successfully set as your access token."]]]
     [:div.footer
      [:div.footer-wrap
       [:div.contact
	[:strong "Contact"] [:br]
	"Support: " [:a {:href "https://github.com/CharlesStain/newsface"}
		     "Project page"]
	[:br]
	"Feedback: " [:a {:href "mailto:alfredo.dinapoli@gmail.com"}
		      "alfredo.dinapoli@gmail.com"]]]]]))