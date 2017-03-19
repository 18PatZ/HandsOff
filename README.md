# Hands Off
An Android app built by Patrick Zhong, Aziza Kydyrmaeva, Adriano Hernandez, and Muhammad Alsharif at MenloHacks II.

## Inspiration
Each year, there are over 100,000 assaults and harassments. Clearly, street harassment is a pressing problem that has been overlooked and undersolved. Our app aims to help fight these occurences.

## What it does
Hands Off allows users to send reports about individuals in their vicinity if they feel nervous or threatened, allowing others to be aware of these individuals and thus steer free of them. Users only have to send a report name and description. The location will be gathered and the threat level of the situation calculated, then displayed to users within 10 meters.

## How we built it
We used Firebase to transfer and store coordinate data in real-time between separate devices. The coordinates themselves were extracted after much effort using Android's LocationManager API. Android's Notifications API also allowed us to send users heads-up notifications when new reports in the vicinity were sent. We utilized IBM Watson's Natural Language Classifier to infer the threat level of the situation by passing a classifier we trained over the report description, and displayed the combined harassment report to the user using TextViews.

## Challenges:
-Brainstorming good ideas<br />
-Working in a new environment (all first-timers in android studio)<br />
-The numerous obstacles encountered while implementing IBM Watson for threat-level recognition

## Accomplishments that we're proud of
-Overcoming above challenges<br />
-Building a nice, aesthetic app that actually combines cohesively each of our features
-Working with machine learning

## What we learned
-We now have more experience in Android app development and working with other Google APIs
-Trying new, daunting things, can end up quite rewarding 
-Exposure to the powerful libraries that IBM Watson provides and what could be done with them.

## What's next for Hands Off
-Adding DYNAMIC maps (real-time changes to route depending on local threat-level)

![alt tag](https://s1.postimg.org/63f823exb/Screen_Shot_2017_03_18_at_8_07_18_PM.png)
![alt tag](https://s1.postimg.org/64p5vigr3/Screen_Shot_2017_03_18_at_8_08_09_PM.png)
