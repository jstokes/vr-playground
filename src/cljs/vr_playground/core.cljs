(ns vr-playground.core
    (:require [reagent.core :as reagent :refer [atom]]
              [reagent.session :as session]
              [secretary.core :as secretary :include-macros true]
              [accountant.core :as accountant]
              [cljsjs.three :as three]))

;; -------------------------
;; Views

(defn home-page []
  [:div [:a-scene
         [:a-sphere {:position "0 1.25 -5"
                     :radius   "1.25"
                     :color "#FE2D5E"}]
         [:a-box {:position "-1 0.5 -3"
                  :rotation "0 45 0"
                  :width "1"
                  :height "1"
                  :depth "1"
                  :color "#4CC3D9"}]
         [:a-cylinder {:position "1 0.75 -3"
                       :radius "0.5"
                       :height "1.5"
                       :color "#FFC65D"}]
         [:a-plane {:position "0 0 -4"
                    :rotation "-90 0 0"
                    :width "4"
                    :height "4"
                    :color "#7BC8A4"}]
         [:a-sky {:color "#ECECEC"}]
         ]])

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
  (reagent/render [current-page] (.getElementById js/document "app")))

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
