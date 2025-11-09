# Pay Now Challenge

An Android sample application showcasing a checkout flow built with Kotlin, Jetpack Compose, Navigation, and a clean modular architecture. The app guides a user through entering credit card details, verifying the card, and showing a verification outcome.

Current local date/time: 2025-11-09 21:10

## Highlights
- 100% Kotlin, Jetpack Compose UI
- Modularized by feature and core layers
- Navigation component for screen flow
- Repository/DataSource pattern for data access
- Hilt DI (via custom build-logic plugin wiring)
- Spotless + ktlint formatting and lint conventions
- Unit and instrumentation tests (Compose UI tests included)

## Project Structure
The project is organized into multiple Gradle modules:

- app
  - Android application entry point and high-level navigation
  - Compose setup/theme
  - NavHost that wires feature modules together

- feature/credit_card
  - UI and logic for entering credit card details
  - ViewModel, validators, constraints, and UI components (number, expiry, CVV, pay button)
  - Sample issuing card assets and string resources

- feature/credit_card_verification
  - Orchestrates verification of a provided credit card (loading/success/failure states)
  - ViewModel emitting VerificationState (in-progress, success with URL, failure)
  - Navigation destinations related to verification flow

- feature/verification_outcome
  - UI that displays the final verification outcome to the user

- core/payment_processor
  - Abstractions and remote data sources for payment processing and configuration
  - Retrofit APIs, repositories, interceptors

- core/creditcard_processor
  - Tokenization-related abstractions and network stack
  - Retrofit APIs, repositories, interceptors

- common
  - Shared utilities, validators, DI wiring used across modules

- build-logic
  - Convention plugins for Android/Kotlin configuration used by modules (Compose, Hilt, Lint, Testing, Spotless, etc.)

Key files you may want to open first:
- app/src/main/java/io/mz/checkout/paynow/PayNowApp.kt – App root Composable
- app/src/main/java/io/mz/checkout/paynow/navigation/PayNowNavHost.kt – Navigation graph
- feature/credit_card/.../CreditCardScreen.kt – Card entry screen
- feature/credit_card_verification/.../VerifiationScreen.kt – Verification UI
- feature/verification_outcome/.../VerificationOutcomeScreen.kt – Outcome UI

## Architecture Overview
- UI: Jetpack Compose with state hoisting and previewable components
- Navigation: Single-Activity with Compose Navigation
- DI: Hilt (applied via custom build-logic convention plugin)
- Data: Repository -> DataSource pattern
- Networking: Retrofit/OkHttp with interceptors in core layers
- Separation of concerns via feature modules for UI and core modules for data/network

## Requirements
- Android Studio Ladybug (or newer)
- JDK 17
- Android Gradle Plugin and Gradle Wrapper are provided via this repo

## Getting Started
1. Clone the repository
2. Open the project in Android Studio
3. Add `payment.request.auth` and `payment.token.auth` into the `local.properties`
4. Let Gradle sync finish
5. Choose an emulator or device running Android 8.0 (API 26) or newer
6Press Run

Alternatively, you can build from the command line:

```bash
./gradlew assembleDebug
```

To install and run on a connected device:

```bash
./gradlew installDebug
adb shell am start -n io.mz.checkout.paynow/.MainActivity
```

## Running Tests
- Unit tests (example in feature/credit_card):

```bash
./gradlew test
```

- Instrumented/android tests:

```bash
./gradlew connectedAndroidTest
```

## Code Style & Lint
This project uses Spotless with ktlint and common Android lint settings via convention plugins.

Common tasks:

```bash
# Check formatting and lint
./gradlew spotlessCheck lint

# Apply formatting
./gradlew spotlessApply
```

## Module Interactions (High Level)
- app
  - hosts navigation and composes feature screens
- feature/credit_card
  - collects user input -> triggers pay/verify action
- feature/credit_card_verification
  - verifies via repositories exposed by core modules
  - reports success/failure -> navigates accordingly
- feature/verification_outcome
  - shows result to user
- core modules (payment_processor, creditcard_processor)
  - define Retrofit APIs, repositories, authorization interceptors
- common
  - shared DI, constraints, validators

## License
Unless otherwise noted in individual files, source is provided under the Apache License, Version 2.0.

```
Copyright 2025 Martin Zangl

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
