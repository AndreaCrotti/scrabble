(ns scrabble.core
    (:require [reagent.core :as reagent]
              [re-frame.core :as re-frame]
              ;; how do I make it happen only on dev?
              [scrabble.events]
              [scrabble.subs]
              [scrabble.views :as views]))

(def debug?
  ^boolean js/goog.DEBUG)

(defn dev-setup []
  (when debug?
    (enable-console-print!)
    (println "dev mode")))

(defn mount-root []
  (re-frame/clear-subscription-cache!)
  (reagent/render [views/main-panel]
                  (.getElementById js/document "app")))

(defn ^:export init []
  (re-frame/dispatch-sync [:initialize-db])
  (dev-setup)
  (mount-root))
