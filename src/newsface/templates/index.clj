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
      [:a {:href "/accesstoken/"} "Access Token"]
      [:a {:href "/suggest/"} "Try it!"]
      [:a {:href "/news/"} "My News"]
      [:a {:href "/contacts/"} "Contacts"]]
     [:div.content
      [:h2 "What is Newsface?"
       [:p "Newsface [News from Facebook] is a news raccomender tool. "
	"It suggests you news which you may be interested in."]]
      [:h2 "Why Newsface is different?"
       [:p "Newsface performs a non-invasive search inside your Facebook "
	"profile and finds your closest friends (several metrics are "
	"used). Then extracts a bunch of keywords based on your "
	"friends tastes and preferences. Finally searches the web, "
	"bringing you news."]]
      [:h2 "May I try Newsface?"
       [:p "Sure. In order to make Newsface works properly you need "
	"to complete these steps: "
	[:ol
	 [:li "Login on "[:a {:href "http://www.facebook.com"} "Facebook"]]
	 [:li "Retrieve an " [:a {:href "/accesstoken/"} "access token"] " for your requests."]
	 [:li [:a {:href "/suggest/"} "Train"] " the system."]
	 [:li "Let Newsface " [:a {:href "/news/"} "suggests"] " you news."]]]]]
     [:div.footer
      [:div.footer-wrap
       [:div.contact
	[:strong "Contact"] [:br]
	"Support: " [:a {:href "https://github.com/CharlesStain/newsface"}
		     "Project page"]
	[:br]
	"Feedback: " [:a {:href "mailto:alfredo.dinapoli@gmail.com"}
		      "alfredo.dinapoli@gmail.com"]]]]]))