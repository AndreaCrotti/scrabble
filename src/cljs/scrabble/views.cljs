(ns scrabble.views
  (:require [re-frame.core :as re-frame :refer [dispatch subscribe]]
            [goog.string :as gstring]
            [cljs.spec :as spec]))

;; the page is composed of
;; - a tile editor: to be able to enter the current situation
;; - the available letters
;;
;; Once the tile and the letters are given, we can provide the various
;; options in graphical form, sorting by the most convenient first

(defn- set-lang [lang]
  (dispatch [:set-language lang]))

(def flag-files
  {:italian "italian.png"
   :english "english.png"})

(defn make-lang [lang current-language]
  (let [selected (= lang current-language)
        png-file (lang flag-files)
        props {:type "image" :src png-file}
        full-props (if selected
                     (assoc props :class "language.selected" )
                     (assoc props :on-click #(dispatch [:set-language lang])))]

    [:input full-props]))

(defn lang-selection []
  (let [current-language (subscribe [:current-language])]
    (fn []
      (into
       [:g
        (make-lang :italian @current-language)
        (make-lang :english @current-language)]))))

(defn main-panel []
  ;; might even not need a function at all here
  (fn []
    ;; make it possible to change the number of players??
    (lang-selection)
    #_[:div "Hello world"]))
