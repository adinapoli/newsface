(ns newsface.templates.contacts
  (:use [hiccup core page-helpers]))


(defn contacts-page []
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
      [:h2 "Who is mantaining Newsface?"
       [:p (str "Newsface is a project started within the course of "
		"Sistemi Intelligenti per Internet, \"Roma Tre\" University, "
		"Rome, Italy.")]]
      [:h2 "May I leave feedbacks?"
       [:p "Sure. See the footer of this site for contacts."]]]
     [:div.footer
      [:div.footer-wrap
       [:div.contact
	[:strong "Contact"] [:br]
	"Support: " [:a {:href "https://github.com/CharlesStain/newsface"}
		     "Project page"]
	[:br]
	"Feedback: " [:a {:href "mailto:alfredo.dinapoli@gmail.com"}
		      "alfredo.dinapoli@gmail.com"]]]]]))