(ns scrabble.core-test
  (:require [cljs.scrabble.views :as views]
            [cljs.test :as t :include-macros true]))


(t/deftest make-tile-test
  (t/is (= [] (views/make-tile 0))))
