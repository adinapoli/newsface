(ns newsface.templates.index
  (:use [hiccup core page-helpers]))


(defn index-page []
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
      [:a {:href "/examples.html"} "Examples"]
      [:a {:href "/"} "Documentation"]
      [:a {:href "/"} "Contribute"]
      [:a {:href "/"} "Donate"]]
     [:div.content
      [:h2 "What is Newsface?"
       [:p (str "Newsface [News from Facebook] is a news raccomender tool."
		"It suggests you news which you may be interested in.")]]
      [:h2 "Why Newsface is different?"
       [:p (str "Newsface performs a non-invasive search inside your Facebook "
		"profile and finds your closest friends (several metrics are "
		"used). Then extracts a bunch of keywords based on your "
		"friends tastes and preferences. Finally searches the web, "
		"bringing you news.")]]
      [:h2 "May I try Newsface?"
       [:p (str "Sure. In order to make Newsface works properly you need "
		"to complete these steps: ")
	[:ol
	 [:li "Login on Facebook."]]]]]
     [:div.footer
      [:div.footer-wrap
       [:div.contact
	[:strong "Contact"] [:br]
	"Support: " [:a {:href "https://github.com/CharlesStain/newsface"}
		     "Project page"]
	[:br]
	"Feedback: " [:a {:href "mailto:alfredo.dinapoli@gmail.com"}
		      "alfredo.dinapoli@gmail.com"]]]]]))