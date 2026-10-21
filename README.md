# Lead Capture App (Native Android)

A native Android application built with **Kotlin** and **Jetpack Compose** that renders a dynamic lead capture form entirely from a configuration list, complete with a domain-agnostic Design System, clean MVVM architecture, pure validation function, and responsive compact/expanded layouts.

---

## 1. How to Build and Run

### Prerequisites
- **JDK**: Java 17
- **Android SDK**: Compile SDK 34, Min SDK 26
- **Gradle**: Uses the included Gradle Wrapper (`./gradlew`)

### From Android Studio
1. Open Android Studio and select **Open** -> choose `d:/leadapp` (or your root project directory).
2. Allow Gradle to sync. (The Gradle settings have `rootProject.name = "leadapp"` and `:app` module pre-configured).
3. Select your run configuration (`app`) and click **Run ▶**.

### Choosing an Emulator / Device
- **Compact Layout (Phone Portrait)**:
  - Run on a standard phone emulator (e.g., **Pixel 7 / Pixel 8**, portrait orientation, width < 600dp).
  - Renders a single-column, full-width responsive form.
  - The focused field stays visible above the keyboard using soft input insets (`adjustResize` + `imePadding()`).
- **Expanded Layout (Tablet / Landscape)**:
  - Run on a tablet emulator (e.g., **Pixel Tablet**, 10-inch) or rotate a phone emulator to **Landscape** (width >= 600dp).
  - Renders the form in a two-column adaptive grid.

### From Command Line / Terminal
- Build Debug APK:
  ```bash
  ./gradlew :app:assembleDebug
  ```
- Install directly to a connected device or emulator:
  ```bash
  ./gradlew :app:installDebug
  ```
- Run unit tests:
  ```bash
  ./gradlew test
  ```

---

## 2. Package Layout

The codebase strictly adheres to the domain separation guidelines:

```
com.vymo.leadapp/
├── MainActivity.kt                                 # Hosts LeadFormScreen inside LeadappTheme
│
├── designsystem/                                   # Domain-agnostic Design System
│   ├── tokens/
│   │   └── DesignTokens.kt                         # Colors, spacing, typography, and breakpoint (600dp)
│   ├── atoms/
│   │   ├── TextFieldAtom.kt                        # Generic single-line text input
│   │   ├── SelectAtom.kt                           # Generic dropdown selector
│   │   ├── TextAreaAtom.kt                         # Generic multi-line text area
│   │   ├── CheckboxAtom.kt                         # Generic checkbox with label
│   │   └── ButtonAtom.kt                           # Generic primary action button
│   └── molecules/
│       └── FieldMolecule.kt                        # Combines Label, Control slot, Hint, & Error message
│
└── lead/                                           # Lead Capture feature package
    ├── model/
    │   ├── FormFieldConfig.kt                      # FieldConfig data class & FieldType enum
    │   ├── LeadFormConfig.kt                       # Config list of lead capture fields
    │   └── LeadValidation.kt                       # Pure validation function: (config, values) -> errors
    ├── viewmodel/
    │   └── LeadFormViewModel.kt                    # StateFlow UI state, focus-loss tracking, submit handling
    └── ui/
        ├── DynamicFormField.kt                     # Maps FormFieldConfig to FieldMolecule + Atom
        ├── LeadForm.kt                             # Adaptive form selecting layout by width breakpoint
        ├── LeadFormCompactLayout.kt                # Owns the 1-column layout (phone portrait)
        ├── LeadFormExpandedLayout.kt               # Owns the 2-column layout (tablet / landscape)
        └── LeadFormScreen.kt                       # Scaffold, system bar insets, submission card
```

---

## 3. Where Design System Elements Live

| Element | Location | Description |
| :--- | :--- | :--- |
| **Tokens** | `designsystem/tokens/DesignTokens.kt` | Color palette, spacing scales, corner radiuses, typography, and `TwoColumnBreakpoint = 600.dp`. |
| **Atoms** | `designsystem/atoms/` | `TextFieldAtom`, `SelectAtom`, `TextAreaAtom`, `CheckboxAtom`, `ButtonAtom`. None of these atoms know what a lead is. |
| **Molecule** | `designsystem/molecules/FieldMolecule.kt` | Composable wrapper rendering label (with `*`), control slot, helper hint, and animated error message. |
| **Form Composable** | `lead/ui/LeadForm.kt` | Reads the config and renders one field per entry, choosing the layout dynamically. |

---

## 4. Where Config and Validation Rules Live

| Element | Location | Description |
| :--- | :--- | :--- |
| **Configuration** | `lead/model/LeadFormConfig.kt` | Defines the 7 form fields: Full name, Email, Lead type, Company name (conditional), Phone, Notes, and Consent. |
| **Validation Rules** | `lead/model/LeadValidation.kt` | Pure function `validateLeadForm(config, values) -> Map<String, String?>`. Contains all validation logic without UI side-effects. |

---

## 5. Which Files Own Compact & Expanded Layouts

- **Compact Layout (Phone Portrait)**:
  - Owned by **`lead/ui/LeadFormCompactLayout.kt`**.
  - Renders single column, full-width fields.
  - Automatically respects system bars (`WindowInsets.statusBars`, `WindowInsets.navigationBars`).
  - Stays above the keyboard with `imePadding()` and reachable submit button.
- **Expanded Layout (Tablet / Landscape)**:
  - Owned by **`lead/ui/LeadFormExpandedLayout.kt`**.
  - Full name & Email share Row 1 (`50% / 50%`).
  - Lead type & Company name share Row 2 (Company name appears smoothly when Lead type is `Company`).
  - Phone occupies Row 3.
  - Notes and Consent span the full width.

---

## 6. Architecture & Features

1. **Pure Dynamic Form**: Adding or removing a field requires only modifying `LeadFormConfig.fields`. Composable code is not written per field.
2. **Conditional Visibility**: Company name dynamically displays and hides with animation when Lead type is toggled between `Individual` and `Company`.
3. **Pure Function Validation**: All validation logic lives in `validateLeadForm`. Atoms and UI components do not contain lead rules.
4. **Focus-Loss Validation**: Errors only display after a user leaves the field (`onFocusChanged(false)`) or when Submit is tapped.
5. **Input Length Restraints**: The phone field actively restricts input to digits only with a hard limit of 10 digits (`maxLength = 10`), preventing the user from entering more than 10 digits.
6. **Clean MVVM**: `LeadFormViewModel` manages state through immutable Kotlin `StateFlow<LeadFormUiState>`, exposing pure events `onFieldValueChange`, `onFieldFocusChanged`, and `onSubmit`.
7. **Submission Presentation & Re-capture**: Submitting validates all visible fields. If valid, the submission card displays all captured key-value pairs. Tapping **"Capture Another Lead"** resets all form fields completely back to blank, clears error states, and restores the fresh entry form.
