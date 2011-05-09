(ns newsface.templates.news
  (:use
   [newsface gdata]
   [hiccup core page-helpers] :reload))


(defn news-page []
  (html5
    [:head
     [:title "Newsface - Suggesting you news based on your Facebook friends"]
     (include-css "/css/style.css")
     (include-js "/js/search.js")]
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
      (for [{h :href t :title} (news-search ["Homefront"])]
	[:ol
	 [:li [:a {:href h} t]]])]
     [:div.footer
      [:div.footer-wrap
       [:div.contact
	[:strong "Contact"] [:br]
	"Support: " [:a {:href "https://github.com/CharlesStain/newsface"}
		     "Project page"]
	[:br]
	"Feedback: " [:a {:href "mailto:alfredo.dinapoli@gmail.com"}
		      "alfredo.dinapoli@gmail.com"]]]]]))