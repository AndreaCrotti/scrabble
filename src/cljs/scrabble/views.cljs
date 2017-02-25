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

(defn lang-selection []
  (letfn [(setlang [lang] (dispatch [:set-language lang]))]
    [:g
     [:input {:type "image" :src "italian.png" :on-click #(setlang :italian)}]
     [:input {:type "image" :src "english.png" :on-click #(setlang :english)}]]))

(defn main-panel []
  ;; might even not need a function at all here
  (fn []
    ;; make it possible to change the number of players??
    (lang-selection)
    #_[:div "Hello world"]))
