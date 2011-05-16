(defproject newsface "1.0.0-SNAPSHOT"
  :description "FIXME: write"
  :dependencies [[org.clojure/clojure "1.2.0"]
                 [org.clojure/clojure-contrib "1.2.0"]
		 [org.clojars.charles-stain/clj-facebook-graph "0.1.0"]
		 [incanter/incanter-charts "1.2.3"]
		 [incanter/incanter-core "1.2.3"]
		 [congomongo "0.1.3-SNAPSHOT"]
		 [enlive "1.0.0"]
		 [compojure "0.6.3"]
		 [hiccup "0.3.5"]
		 [scriptjure "0.1.22"]]
  :dev-dependencies [[swank-clojure "1.3.0"]
		             [lein-ring "0.3.2"]]
  :ring {:handler newsface.core/newsface})
