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
   (for [i (range 250)]
     (let [rnd-between (fn [from to] (+ from (* (- to from) (rand))))
           stringify   (fn [& args] (str/join " " args))

           x (rnd-between -10 10)
           y (rnd-between -10 10)
           z (rnd-between -10 10)

           x2 (rnd-between -10 10)
           y2 (rnd-between -10 10)
           z2 (rnd-between -10 10)

           r1 (rnd-between 0 360)
           r2 (rnd-between 0 360)
           r3 (rnd-between 0 360)]
       [:a-box {:depth    "0.1"
                :height   "0.1"
                :width    "0.1"
                :rotation (stringify r1 r2 r3)
                :color    (rand-nth ["black" "red" "green" "blue" "orange" "yellow"])}
        [:a-animation {:attribute "position"
                       :from      (stringify x y z)
                       :to        (stringify x2 y2 z2)
                       :dur       (rnd-between 1000 20000)
                       :repeat    "indefinite"
                       :direction "alternate"}]]))

   [:a-sky {:color "#73F7DD"}]
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
