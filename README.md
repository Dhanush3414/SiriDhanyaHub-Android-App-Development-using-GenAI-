# 🌾 Siri-Dhanya Hub

## AI Powered Millet Farming and Recipe Assistant

Siri-Dhanya Hub is an AI-powered Android application developed to support millet farmers and consumers by providing real-time market prices, healthy millet recipes, and AI-based agricultural guidance. The application promotes Karnataka’s traditional millets (Siri-Dhanya) such as Navane, Sajje, and Baragu using modern Android and Generative AI technologies.

---

#  Project Overview

Millets are highly nutritious and environmentally sustainable crops, but many farmers and consumers lack proper awareness about market prices, farming guidance, and millet-based healthy food preparation.

Siri-Dhanya Hub solves this problem by offering:

* Real-time millet market price information
* AI-powered farming and recipe assistance
* Millet recipe suggestions
* Educational agricultural content
* Easy-to-use Android interface

The application combines Artificial Intelligence, Firebase services, and Android technologies to create a smart agriculture support platform.

---

#  Problem Statement

Farmers growing millets in Karnataka often face challenges in accessing updated market prices from different cities and markets. Due to lack of digital awareness and centralized information systems, many small-scale farmers are unable to make profitable selling decisions.

At the same time, consumers are not fully aware of:

* The health benefits of millets
* Traditional millet recipes
* Sustainable food practices

There is a need for a smart digital platform that bridges the gap between farmers, consumers, and agricultural information.

---

#  Objectives

* Provide real-time millet market prices
* Promote millet consumption and healthy food habits
* Support farmers using AI-powered assistance
* Create awareness about traditional Karnataka millets
* Improve digital accessibility in agriculture
* Build a user-friendly Android application

---

#  Features

##  Market Price Module

* Displays millet prices from various cities/markets
* Helps farmers compare prices before selling
* Supports better agricultural decision-making

##  Millet Recipe Module

* Provides recipes using:

  * Navane
  * Sajje
  * Baragu
  * Other traditional millets
* Includes ingredients and preparation steps
* Promotes healthy eating habits

##  AI Chatbot Assistant

* Powered by Google Gemini API
* Answers farming-related questions
* Suggests recipes and agricultural guidance
* Provides smart user interaction

##  User Authentication

* Firebase Authentication support

##  Awareness & Information Module

* Educational information about:

  * Millet benefits
  * Farming practices
  * Nutrition
  * Sustainable agriculture

---

#  Technologies Used

## Frontend

* Android Studio
* XML Layouts
* Kotlin

## Backend & Database

* Firebase Realtime Database
* Firebase Firestore
* Firebase Authentication

## AI & APIs

* Google Gemini API

## Libraries & Components

* RecyclerView
* CardView
* Material Design Components
* Glide/Picasso

---

# Software Requirements

* Android Studio Hedgehog or later
* JDK 17+
* Android SDK
* Firebase Project
* Internet Connection

---

#  Hardware Requirements

* Minimum 4GB RAM
* Intel i3 or higher processor
* Android Device / Emulator

---

#  Project Structure

```
SiriDhanyaHub/
│
├── app/
│   ├── java/
│   ├── res/
│   │   ├── layout/
│   │   ├── drawable/
│   │   ├── values/
│   │   └── mipmap/
│   ├── AndroidManifest.xml
│   └── build.gradle
│
├── gradle/
├── google-services.json
├── build.gradle
└── README.md
```

---

#  Installation Steps

## Step 1: Clone the Repository

```bash
git clone https://github.com/your-username/SiriDhanyaHub.git
```

## Step 2: Open in Android Studio

* Open Android Studio
* Click on “Open Existing Project”
* Select the project folder

## Step 3: Configure Firebase

1. Create a Firebase project
2. Add Android application
3. Download `google-services.json`
4. Place the file inside:

```bash
app/google-services.json
```

## Step 4: Add Gemini API Key

Add your Gemini API key inside:

```properties
local.properties
```

Example:

```properties
GEMINI_API_KEY=YOUR_API_KEY
```

## Step 5: Sync Gradle

Click:

```bash
Sync Project with Gradle Files
```

## Step 6: Run the Application

* Connect Android device/emulator
* Click Run 

---

#  Firebase Setup

Enable the following Firebase services:

* Firebase Authentication
* Firebase Realtime Database
* Firebase Firestore


---

#  Gemini API Integration

The application uses Google Gemini API for:

* AI chatbot responses
* Farming guidance
* Recipe suggestions
* User interaction

---

#  Screenshots

## Home Screen

* Displays application dashboard

## Market Price Page

* Shows millet prices

## Recipe Module

* Displays millet recipes

## AI Chatbot

* AI-powered assistant interface

## Health Benefits

* Provide health benefits od Siri-Dhanya

## Buy

* Contact details of Mandi Farmers


---

#  Expected Outcomes

* Better market awareness for farmers
* Increased millet consumption
* Improved agricultural digitalization
* AI-assisted farming support
* Enhanced user awareness about healthy food habits

---

#  Future Enhancements

* Multi-language support

  * Kannada
  * English
  * Hindi

* Voice assistant support

* Live weather updates

* Government scheme notifications

* Online millet marketplace

* Farmer-to-consumer communication platform

* AI image recognition for crop diseases

---

#  Learning Outcomes

Through this project, the following skills were developed:

* Android Application Development
* Firebase Integration
* REST API Integration
* Generative AI Integration
* UI/UX Design
* Database Management
* Problem Solving
* Team Collaboration

---

#  Use Cases

## Farmers

* Check market prices
* Get farming guidance
* Access agricultural information

## Consumers

* Learn millet recipes
* Understand health benefits
* Explore sustainable food options

## Students & Researchers

* Study AI-powered agriculture applications
* Learn Android + Firebase integration

---

#  Advantages

* User-friendly interface
* AI-powered assistance
* Real-time information
* Secure authentication
* Promotes sustainable agriculture
* Encourages healthy lifestyle

---

#  Non-Functional Requirements

* High performance
* Secure authentication
* Easy navigation
* Mobile responsive design
* Reliable internet connectivity support

---

#  Testing

The application was tested for:

* Login functionality
* Firebase database connectivity
* API response handling
* User interface responsiveness
* Error handling
* Internet connectivity

---

#  References

* Android Developer Documentation
* Firebase Documentation
* Google Gemini API Documentation
* Material Design Guidelines


---

#  License

This project is developed for educational and research purposes.

---

#  Conclusion

Siri-Dhanya Hub is a smart Android application that combines Artificial Intelligence and agriculture to support millet farmers and consumers. The project helps improve digital accessibility, agricultural awareness, and healthy food practices through AI-powered features, Firebase integration, and modern mobile technologies.
