(ns scrabble.core-test
  (:require #_[cljs.scrabble.core :as sut]
            [cljs.test :as t :include-macros true]))


(t/deftest try-out-test
  (t/testing "Check"
    (t/is (= 1 (+ 0 1)))))
