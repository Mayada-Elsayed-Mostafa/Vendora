# 🛍️ Vendora App


Vendora is a mobile commerce (m-Commerce) Android application designed to provide users with a seamless online shopping experience by integrating with the Shopify services.


## 📱 Project Overview

The application allows authenticated users to browse products from various vendors, search and filter items, manage their shopping cart and wishlist, and complete the full e-commerce cycle. Including placing orders and integrating payment with Paymob Gateway.

## 🚀 Features

- 🔍 Search products by name
- 🏷️ Filter products by price, category, or brand
- 💖 Add/remove products to/from wishlist
- 🛒 Add/remove products to/from cart
- 🧾 View product details with images, price, rating, description
- 🧑 Personalized account section with orders, favorites, and settings
- 🌍 Currency conversion using external API
- 📍 Address management 
- 💸 Online payment (with discount code) or Cash on Delivery
- 🧾 Order confirmation via email
- 🔐 Firebase Authentication with email verification
- 🌐 RESTful API + ShopifyDB integration

## 🧩 Architecture & Tech Stack

- **Language**: Kotlin
- **UI**: Jetpack Compose
- **Architecture**: MVVM
- **DI**: Hilt
- **Networking**: Retrofit
- **Local Storage**: Room DB
- **Authentication**: Firebase Auth
- **Currency API**: Exchange Rates API
- **Payment Gateway**: Paymob 
- **API Source**: Shopify REST API 

## 🗂️ App Structure

- `Home Tab`: Search, Ads, Brands, Product listing by brand
- `Category Tab`: Main/Sub categories, product listing by category
- `Me Tab`: Auth status check, user orders, wish list, cart access, and settings


## 👥 Team Members & Responsibilities

| Name              | Responsibilities                                                                 |
|-------------------|-----------------------------------------------------------------------------------|
| Mohamed Khaled    | 💳 Discount code <br> 🛒 Shopping Cart <br> 💳 Payment <br> 🧩 Ads <br> ⚙️ Settings screen  |
| Zeyad Ma'moun     | 🏠 Home screen UI <br> 🏷 Brand listing & filtering <br> 📂 Categories screen|💳 Orders & Confirmation Email |
| Mayada Elsayed    |🔐 Firebase Authentication (Sign in/Sign up) & Verification email <br> ⭐ Wishlist <br> 🔍 Search functionality  <br> 📦 Product details UI |



## 📸 UI 
![ff](https://github.com/user-attachments/assets/db73fae4-4a32-42dd-8723-9baae738f2eb)



## 🧪 Setup & Installation

1. Clone the repo:

```bash
git clone https://github.com/Mayada-Elsayed-Mostafa/Vendora.git
