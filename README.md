# Pokopy

**Pokopy** is an Android application that connects to the Pokémon API and provides users with a way to explore Pokémon data in an intuitive mobile interface. It demonstrates modern Android development practices with Kotlin, API integration, and structured app architecture.

## Overview
The app retrieves data from the Pokémon API and presents it through a responsive UI. Users can browse Pokémon, view details, and explore attributes like abilities, evolution chains, and habitats. It showcases how to consume and model API data on Android while maintaining clean code organization.

## User Experience
- **Home Screen:** Entry point into the app with navigation to the Pokémon catalog  
- **Pokédex Browsing:** List of Pokémon pulled dynamically from the API  
- **Detail Views:** Displays comprehensive information such as stats, types, abilities, and evolution data  
- **Smooth Interaction:** Structured Kotlin classes and models ensure reliable data mapping and presentation  

## Features
- API integration with the official Pokémon API  
- Kotlin-based architecture with modular package structure  
- Data models for Pokémon attributes, evolution chains, abilities, and game indices  
- Organized code in `PokemonAPI/model/response` and `PokemonAPI/model/entries` for scalable data handling  
- Firebase/Google Services support (configured via `google-services.json`)  

## Tech Stack
- **Language:** Kotlin  
- **Framework:** Android SDK  
- **API:** Pokémon API (RESTful)  
- **Other:** Firebase services, Gradle build system  
