(ns jan25.core
  (:require [dommy.template :as template]
            [domina :as dom]
            [domina.events :as ev]
            [domina.xpath :as x]
            [domina.css :as css]))

(def grid
  (atom
   [[0 0 0 0 0 0 0]
    [0 0 0 0 0 0 0]
    [0 0 0 1 0 0 0]
    [0 0 0 0 1 0 0]
    [0 0 1 1 1 0 0]
    [0 0 0 0 0 0 0]
    [0 0 0 0 0 0 0]]))

(defn neighbours [grid [x y]]
  (let [rs (count grid)
        cs (count (first grid))]
    (->> [[(+ x 1) (+ y 1)] [x (+ y 1)] [(- x 1) (+ y 1)]
          [(+ x 1) y] [(- x 1) y]
          [(+ x 1) (- y 1)] [x (- y 1)] [(- x 1) (- y 1)]]
         (map (fn [[i j]] [(mod i rs) (mod j cs)])))))

(defn destiny [grid [x y]]
  (let [cell (get-in grid [x y])
        ns   (->> (neighbours grid [x y])
                  (map #(get-in grid %))
                  (reduce +))]
    (cond
     (and (= cell 1) (< ns 2)) 0
     (and (= cell 1) (< ns 4)) 1
     (and (= cell 1) (< 3 ns)) 0
     (and (= cell 0) (= ns 3)) 1
     :else 0)))

(defn update-grid [grid]
  (let [rs (count grid)
        cs (count (first grid))
        g  (mapv (fn [i] (mapv (fn [j] (destiny grid [i j])) (range cs))) (range rs))]
    g))

(defn ^:export render-grid []
  (let [g @grid
        m {0 "dead" 1 "alive"}]
    (dommy.template/node
     (into [:div]
           (for [r g]
             (into [:div.row]
                   (for [c r]
                     [:span.cell {:class (m c)}])))))))

(defn ^:export tob-render []
  (dom/append! (x/xpath "//div[@id='grid']") (.-outerHTML (render-grid))))

(defn ^:export update-grid! []
  (swap! grid update-grid))

(defn ^:export next-iteration []
  (do
    (dom/destroy! (x/xpath "//div[@id='grid']/div"))
    (tob-render)
    (update-grid!)))

(defn ^:export init []
  (when (and js/document
             (.-getElementById js/document))
    (next-iteration)
    (ev/listen! (dom/by-id "test-anchor") :click next-iteration)))

(set! (.-onload js/window) init)
