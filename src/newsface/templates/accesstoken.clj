(ns newsface.templates.accesstoken
  (:use [hiccup core page-helpers]))


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
      [:a {:href "/contacts/"} "Contacts"]]
     [:div.content
      [:h2 "What is an Access Token?"
       [:p (str "An access token is an alphanumerical code used by "
		"Facebook in order to authenticate web request to "
		"its functionality.")]]
      [:h2 "How can I get one, and why this can't be automatic?"
       [:p "Newsface is still in beta."]]]
     [:div.footer
      [:div.footer-wrap
       [:div.contact
	[:strong "Contact"] [:br]
	"Support: " [:a {:href "https://github.com/CharlesStain/newsface"}
		     "Project page"]
	[:br]
	"Feedback: " [:a {:href "mailto:alfredo.dinapoli@gmail.com"}
		      "alfredo.dinapoli@gmail.com"]]]]]))