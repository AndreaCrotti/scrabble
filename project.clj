(defproject scrabble "0.1.0-SNAPSHOT"
  :description "Help finding good words for scrabble"
  :url "https://github.com/AndreaCrotti/scrabble"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/tools.cli "0.3.5"]
                 [org.clojure/data.json "0.2.6"]
                 [org.clojure/core.async "0.2.395"]
                 [http-kit "2.2.0"]
                 [clj-jwt "0.1.1"]
                 [compojure "1.5.2"]
                 [environ "1.1.0"]
                 [ring/ring-json "0.4.0"]
                 [metosin/ring-http-response "0.8.1"]
                 [ring-middleware-format "0.7.0" :exclusions [ring]]
                 [ring/ring-defaults "0.2.2"]
                 [org.clojure/math.combinatorics "0.1.4"]]

  :uberjar-name "clojure-getting-started-standalone.jar"
  :plugins [[lein-ring "0.8.13"]
            [environ/environ.lein "0.3.1"]]
  :hooks [environ.leiningen.hooks]
  :ring {:handler scrabble.api/app
         :auto-reload? true
         :auto-refresh? true}
  :main ^:skip-aot scrabble.core
  :target-path "target/%s"
  :profiles
  {:dev
   {:plugins [[lein-figwheel "0.5.4-4"]]
    :dependencies [[javax.servlet/servlet-api "2.5"]
                   [ring-mock "0.1.5"]
                   [figwheel "0.5.4-4"]
                   [figwheel-sidecar "0.5.4-4"]]}})
