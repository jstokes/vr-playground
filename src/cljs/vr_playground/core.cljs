(ns vr-playground.core
  (:require [reagent.core :as reagent :refer [atom]]
            [reagent.session :as session]
            [secretary.core :as secretary :include-macros true]
            [accountant.core :as accountant]
            [clojure.string :as str]))

;; -------------------------
;; Views

(defn home-page []
  [:a-scene
   (for [i (range 100)]
     (let [rnd-between (fn [from to] (+ from (* (- to from) (rand))))
           stringify   (fn [& args] (str/join " " args))

           rad (rnd-between 0.1 0.3)

           x (rnd-between -10 10)
           y (rnd-between -10 10)
           z (rnd-between -10 10)

           x2 (rnd-between -10 10)
           y2 (rnd-between -10 10)
           z2 (rnd-between -10 10)]
       ^{:key i}
       [:a-sphere {:color    (rand-nth ["white" "red" "green" "blue" "orange" "yellow"])
                   :position (stringify x y z)
                   :radius   rad}
        [:a-animation {:attribute "position"
                       :from      (stringify x y z)
                       :to        (stringify x2 y2 z2)
                       :dur       (rnd-between 1000 20000)
                       :repeat    "indefinite"
                       :easing    "ease-circ"
                       :direction "alternate"}]]))

   [:a-light {:type      "ambient"
              :color     "white"
              :intensity 0.2
              :decay     4.0
              :position  "-5.0 1 -1"}]

   [:a-light {:type      "ambient"
              :color     "white"
              :intensity 0.2
              :decay     4.0
              :position  "5 1 -1"}]

   [:a-sky {:color "black"}]
   ])

(defn about-page []
  [:div [:h2 "About vr-playground"]
   [:div [:a {:href "/"} "go to the home page"]]])

(defn current-page []
  [:div [(session/get :current-page)]])

;; -------------------------
;; Routes

(secretary/defroute "/" []
  (session/put! :current-page #'home-page))

(secretary/defroute "/about" []
  (session/put! :current-page #'about-page))

;; -------------------------
;; Initialize app

(defn mount-root []
  (reagent/render [current-page] (.getElementById js/document "scene-container")))

(defn init! []
  (accountant/configure-navigation!
    {:nav-handler
     (fn [path]
       (secretary/dispatch! path))
     :path-exists?
     (fn [path]
       (secretary/locate-route path))})
  (accountant/dispatch-current!)
  (mount-root))
