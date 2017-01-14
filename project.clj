(defproject scrabble "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/tools.cli "0.3.5"]
                 [org.clojure/data.json "0.2.6"]
                 [org.clojure/core.async "0.2.395"]
                 [clj-jwt "0.1.1"]
                 [compojure "1.5.2"]
                 [environ "1.1.0"]
                 [ring/ring-defaults "0.2.2"]
                 [org.clojure/math.combinatorics "0.1.4"]]

  :plugins [[lein-ring "0.8.13"]]
  :ring {:handler scrabble.api/app
         :auto-reload? true
         :auto-refresh? true}
  :main ^:skip-aot scrabble.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
