(defproject scrabble "0.1.0-SNAPSHOT"
  :description "Help finding good words for scrabble"
  :url "https://github.com/AndreaCrotti/scrabble"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[bk/ring-gzip "0.2.1"]
                 [clj-jwt "0.1.1"]
                 [compojure "1.5.2"]
                 [environ "1.1.0"]
                 [garden "1.3.2"]
                 [http-kit "2.2.0"]
                 [metosin/ring-http-response "0.8.1"]
                 [mutant "0.1.0"]
                 [org.clojure/clojure "1.8.0"]
                 [org.clojure/clojurescript "1.9.494"]
                 [org.clojure/core.async "0.3.441"]
                 [org.clojure/data.json "0.2.6"]
                 [org.clojure/math.combinatorics "0.1.4"]
                 [org.clojure/tools.cli "0.3.5"]
                 [re-frame "0.9.1"]
                 [reagent "0.6.0"]
                 [ring "1.5.1"]
                 [ring-middleware-format "0.7.2" :exclusions [ring]]
                 [ring.middleware.logger "0.5.0"]
                 [ring/ring-defaults "0.2.2"]
                 [ring/ring-json "0.4.0"]
                 [devcards "0.2.2"]
                 [cljs-ajax "0.5.8"]
                 [secretary "1.2.3"]
                 [org.clojure/test.check "0.9.0"]]

  :uberjar-name "scrabble.jar"
  :plugins [[lein-ring "0.8.13"]
            [environ/environ.lein "0.3.1"]
            [lein-cljsbuild "1.1.4"]
            [lein-garden "0.2.8"]]

  :min-lein-version "2.5.3"

  :source-paths ["src/clj" "src/cljc"]
  :test-paths ["test/clj" "test/cljc"]

  :clean-targets ^{:protect false} ["resources/public/js/compiled" "target"
                                    "test/js"
                                    "resources/public/css"]

  :figwheel {:css-dirs ["resources/public/css"]
             ;; :ring-handler scrabble.user/http-handler
             :open-file-command "lein_opener.sh"
             :server-logfile "log/figwheel.log"}

  :garden {:builds [{:id           "screen"
                     :source-paths ["src/clj"]
                     :stylesheet   scrabble.css/screen
                     :compiler     {:output-to     "resources/public/css/screen.css"
                                    :pretty-print? true}}]}

  ;; add a default build and a way to execute phantomjs
  ;; automatically if possible
  :doo {:build "test"}

  :hooks [environ.leiningen.hooks]
  :ring {:handler scrabble.api/app
         :auto-reload? true
         :auto-refresh? true}
  :main ^:skip-aot scrabble.cli
  :target-path "target/%s"
  :profiles

  {:dev
   {:plugins [[lein-figwheel "0.5.9"]
              [lein-doo "0.1.7"]]

    :repl-options {:nrepl-middleware [cemerick.piggieback/wrap-cljs-repl]}
    :dependencies [[binaryage/devtools "0.9.0"]
                   [com.cemerick/piggieback "0.2.1"]
                   [figwheel "0.5.9"]
                   [figwheel-sidecar "0.5.9"]
                   [javax.servlet/servlet-api "2.5"]
                   [lambdaisland/garden-watcher "0.2.0"]
                   ;; dependencies for the reloaded workflow
                   [ns-tracker "0.3.1"]
                   [re-frisk "0.3.2"]
                   [reloaded.repl "0.2.3"]
                   [ring-mock "0.1.5"]]}}

  :cljsbuild
  {:builds
   [{:id           "dev"
     :source-paths ["src/cljs"]
     :figwheel     {:on-jsload "scrabble.core/mount-root"}
     :compiler     {:main                 scrabble.core
                    :output-to            "resources/public/js/compiled/app.js"
                    :output-dir           "resources/public/js/compiled/out"
                    :asset-path           "js/compiled/out"
                    :source-map-timestamp true
                    :preloads             [devtools.preload]
                    :external-config      {:devtools/config {:features-to-install :all}}
                    }}

    {:id           "min"
     :source-paths ["src/cljs"]
     :compiler     {:main            scrabble.core
                    :output-to       "resources/public/js/compiled/app.js"
                    :optimizations   :advanced
                    :closure-defines {goog.DEBUG false}
                    :pretty-print    false}}

    {:id           "test"
     :source-paths ["src/cljs" "test/cljs"]
     :compiler     {:main          scrabble.runner
                    :output-to     "resources/public/js/compiled/test.js"
                    :output-dir    "resources/public/js/compiled/test/out"
                    :optimizations :none}}
    ]}
  )
