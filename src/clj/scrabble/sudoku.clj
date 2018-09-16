(ns scrabble.sudoku
  (:require [clojure.core.logic :as l]
            [clojure.core.logic.fd :as fd]))

(defn get-square
  [rows x y]
  (for [x (range x (+ x 3))
        y (range y (+ y 3))]

    (get-in rows [x y])))

(defn init
  [vars hints]
  (if (seq vars)
    (let [hint (first hints)]
      (l/all
       (if-not (zero? hint)
         (l/== (first vars) hint)
         l/succeed)
       (init (next vars) (next hints))))

    l/succeed))

(defn sudokufd
  [hints]
  (let [vars (repeatedly 81 l/lvar)
        rows (->> vars (partition 9) (map vec) (into []))
        cols (apply map vector rows)
        sqs (for [x (range 0 9 3)
                  y (range 0 9 3)]

              (get-square rows x y))]

    (l/run 1 [q]
      (l/== q vars)
      (l/everyg #(fd/in % (apply fd/domain (range 1 10))) vars)
      (init vars hints)
      (l/everyg fd/distinct rows)
      (l/everyg fd/distinct cols)
      (l/everyg fd/distinct sqs)
      )))

(sudokufd (vec (repeat 81 0)))
