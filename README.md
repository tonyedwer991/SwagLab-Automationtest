<div align="center">

# SauceDemo UI Automation Testing Framework

A Selenium WebDriver + TestNG automation framework for the [SauceDemo](https://www.saucedemo.com/) e-commerce demo application, built on the **Page Object Model** with **JSON-driven test data**.

</div>

---

## Table of Contents

- [Overview](#overview)
- [Technology Stack](#technology-stack)
- [Architecture](#architecture)
- [Test Scenarios](#test-scenarios)
- [Test Coverage Summary](#test-coverage-summary)
- [Data-Driven Testing](#data-driven-testing)
- [Synchronization Strategy](#synchronization-strategy)
- [Assertions](#assertions)
- [Screenshot Capture](#screenshot-capture)
- [WebDriver Event Listener](#webdriver-event-listener)
- [Test Setup & Teardown](#test-setup--teardown)
- [Getting Started](#getting-started)
- [Running the Tests](#running-the-tests)
- [Key Concepts Demonstrated](#key-concepts-demonstrated)
- [Roadmap](#roadmap)
- [Author](#author)
- [License](#license)

---

## Overview

This project automates core user journeys on the SauceDemo application, including authentication, cart management, checkout, and cross-session state behavior. It is designed as a **maintainable framework**, not a collection of standalone scripts — locators, actions, and assertions are deliberately separated across distinct layers so that the suite stays easy to extend and debug as it grows.

**Scope covered:**

- Login and authentication (valid, invalid, missing-field scenarios)
- Shopping cart management (add, remove, verify order and contents)
- Product price extraction and total calculation
- Checkout flow, including edge cases (empty cart)
- Cart state persistence across logout/login
- Social media link verification
- Data-driven test execution via external JSON
- Explicit synchronization, soft assertions, and screenshot capture on failure

---

## Technology Stack

| Technology | Purpose |
|---|---|
| Java | Core language |
| Selenium WebDriver | Browser automation |
| TestNG | Test execution, assertions, grouping |
| Maven | Dependency and build management |
| JSON (org.json) | External, version-controlled test data |
| WebDriverWait | Explicit synchronization |
| Selenium `WebDriverListener` | Centralized event handling (click logging, auto-wait) |

---

## Architecture

The framework follows the **Page Object Model (POM)**: each page of the application under test is represented by a dedicated class that owns its locators and low-level interactions, while test classes own orchestration and assertions.

```text
SauceDemo-Automation
│
├── src
│   ├── main
│   │   ├── PAGES
│   │   │   ├── Loginpage.java
│   │   │   ├── InventroryPage.java
│   │   │   └── CartPage.java
│   │   │
│   │   └── utils
│   │       └── DataDriven.java
│   │
│   └── test
│       ├── BASE
│       │   └── Basetests.java
│       │
│       ├── tests
│       │   ├── CartTest.java
│       │   └── Socialmedia.java
│       │
│       └── Utilities
│           └── MyListenere.java
│
├── testData.json
├── pom.xml
└── README.md
```

### Page Object Responsibilities

**`Loginpage`**
- Entering username and password
- Submitting the login form

**`InventroryPage`**
- Product locators and add-to-cart actions
- Reading individual product prices and calculating totals
- Opening the shopping cart
- Navigation menu and logout actions
- Social media link locators

**`CartPage`**
- Reading cart contents and item count
- Removing individual products
- Checkout form entry (name, postal code)
- Reading the checkout subtotal

---

## Test Scenarios

### 1. Social Media Links
**Test:** `validteSocialmedialinks`

Verifies that each footer social icon navigates to the correct external destination after login.

| Step | Action |
|---|---|
| 1 | Log in with valid credentials |
| 2 | Click the LinkedIn icon and verify the resulting URL |
| 3 | Click the Facebook icon and verify the resulting URL |
| 4 | Click the X (Twitter) icon and verify the resulting URL |

**Expected result:** Each link navigates to the corresponding Sauce Labs social media page.

---

### 2. Verify Empty Cart
**Test:** `EmptyCartTest`

| Step | Action |
|---|---|
| 1 | Log in with valid credentials |
| 2 | Open the shopping cart |
| 3 | Assert the cart item count is `0` |

---

### 3. Add Three Specific Products (Data-Driven)
**Test:** `Add3SpecificProducts`

Product names are never hardcoded in the test method — they are loaded from `testData.json`:

```json
"productName": [
    "Sauce Labs Backpack",
    "Sauce Labs Bolt T-Shirt",
    "Sauce Labs Onesie"
]
```

| Step | Action |
|---|---|
| 1 | Log in with valid credentials |
| 2 | Read product names from JSON |
| 3 | Add all products to the cart |
| 4 | Open the cart and read the actual contents |
| 5 | Compare actual vs. expected products |

**Verifies:** the correct products were added, the correct count, and that insertion order is preserved.

---

### 4. Remove a Product
**Test:** `RemoveoneProduct`

| Step | Action |
|---|---|
| 1 | Log in and add the 3 JSON-driven products |
| 2 | Open the cart and remove `Sauce Labs Bolt T-Shirt` |
| 3 | Verify the remaining two products still show **Remove** |
| 4 | Return to the Inventory page |
| 5 | Verify the removed product's button reverted to **Add to cart** |

**Expected result:**

```text
Sauce Labs Backpack      → Remove
Sauce Labs Onesie        → Remove
Sauce Labs Bolt T-Shirt  → Add to cart
```

---

### 5. Verify Product Subtotal
**Test:** `SubtotalProduct`

Validates the checkout subtotal against an independently calculated expected value, rather than trusting the application's own displayed figure.

| Step | Action |
|---|---|
| 1 | Log in and read product names from JSON |
| 2 | Read each product's price from the Inventory page |
| 3 | Convert each price from display text to `double` |
| 4 | Sum the individual prices to calculate an expected subtotal |
| 5 | Add the products to the cart and proceed through checkout |
| 6 | Read the displayed Item Total |
| 7 | Assert calculated subtotal == displayed subtotal |

**Example:**

```text
Sauce Labs Backpack       $29.99
Sauce Labs Bolt T-Shirt   $15.99
Sauce Labs Onesie          $7.99
-----------------------------------
Expected Subtotal          $53.97
```

---

### 6. Checkout With an Empty Cart
**Test:** `checkoutemptyCartTest`

Exercises the application's behavior when checkout is attempted with no products in the cart.

| Step | Action |
|---|---|
| 1 | Log in |
| 2 | Open an empty cart |
| 3 | Click Checkout |
| 4 | Assert the resulting application behavior |

**Observed behavior:** SauceDemo currently allows navigation to the checkout information page even with an empty cart.

> This test doubles as a **behavioral/requirements check**: if the intended business rule is that checkout should be blocked on an empty cart, this test surfaces a gap between expected and actual application behavior rather than silently passing.

---

### 7. Cart State After Logout/Login
**Test:** `cartStateAfterLogoutLoginTest`

Verifies actual cart persistence behavior across a logout/login cycle for the same account, rather than assuming an outcome.

| Step | Action |
|---|---|
| 1 | Log in and add the configured products |
| 2 | Log out |
| 3 | Log back in with the same credentials |
| 4 | Open the cart and read its contents |
| 5 | Compare against the pre-logout cart contents |

**Note:** This scenario is intentionally written to assert against *observed* application behavior — verify manually once before relying on the assertion, since cart persistence is an implementation detail, not a documented guarantee.

---

## Test Coverage Summary

| # | Scenario | Test Class | Priority | Group |
|--:|---|---|--:|---|
| 1 | Verify Social Media Links | `Socialmedia` | 1 | Regression |
| 2 | Verify Empty Cart | `CartTest` | 1 | Smoke |
| 3 | Add 3 Specific Products | `CartTest` | 2 | Smoke |
| 4 | Remove One Product | `CartTest` | 3 | Smoke |
| 5 | Verify Product Subtotal | `CartTest` | 4 | Smoke |
| 6 | Checkout With Empty Cart | `CartTest` | 5 | Smoke |
| 7 | Cart State After Logout/Login | `CartTest` | 6 | Smoke |

---

## Data-Driven Testing

Test data is fully externalized from automation logic, so scenarios can be extended or reconfigured without touching Java code.

```json
{
  "validLogin": {
    "username": "standard_user",
    "password": "secret_sauce"
  },
  "invalidLogin": {
    "username": "error_user",
    "password": "0000"
  },
  "noPasswordLogin": {
    "username": "error_user",
    "password": ""
  },
  "productName": [
    "Sauce Labs Backpack",
    "Sauce Labs Bolt T-Shirt",
    "Sauce Labs Onesie"
  ],
  "RemaningName": [
    "Sauce Labs Backpack",
    "Sauce Labs Onesie"
  ]
}
```

Tests retrieve this data through the `DataDriven` utility:

```java
JSONObject validLogin = DataDriven.jsonReader("validLogin");
List<String> productNames = DataDriven.jsonArrayReader("productName");
```

---

## Synchronization Strategy

The framework relies exclusively on **explicit waits** via `WebDriverWait` and `ExpectedConditions` — no `Thread.sleep()` is used anywhere in the codebase.

```java
wait.until(ExpectedConditions.elementToBeClickable(locator));
```

This reduces flakiness caused by elements taking time to render, become visible, or become interactable, without introducing fixed, wasteful delays.

---

## Assertions

The suite uses TestNG's `SoftAssert` so that every check within a test method is executed and reported, rather than stopping at the first failure.

```java
softAssert.assertEquals(
    actualProducts,
    expectedProducts,
    "Cart items should match the products added"
);

softAssert.assertAll();
```

---

## Screenshot Capture

`Basetests` captures a screenshot after every test execution, stored under:

```text
target/Screenshots/
```

This provides visual evidence of the final browser state and aids debugging of failed runs, including in CI environments where the browser session is not directly observable.

---

## WebDriver Event Listener

`Utilities/MyListenere` implements Selenium's `WebDriverListener` interface and is attached to the driver via `EventFiringDecorator` at creation time. Once attached, its behavior applies transparently to every interaction made through the driver, across all page objects and tests:

- Logs the target element's visible text (with attribute-based fallbacks) before each click, for easier failure diagnosis
- Waits for the page to reach a stable, fully-loaded state after each click, reducing the need for manual waits scattered through test code

---

## Test Setup & Teardown

`Basetests` centralizes browser lifecycle management so individual test classes contain no driver setup/teardown logic.

**Before each test:**
- Launch Chrome (with security-prompt suppression configured)
- Maximize the browser window
- Initialize `WebDriverWait` and `SoftAssert`
- Navigate to the SauceDemo base URL

**After each test:**
- Capture a screenshot
- Terminate the WebDriver session

---

## Getting Started

### Prerequisites

- Java JDK 17+
- Maven
- Google Chrome
- IntelliJ IDEA (or another Java IDE)
- Git

### Installation

```bash
git clone <repository-url>
cd <project-folder>
```

Open the project in your IDE and allow Maven to resolve dependencies automatically.

---

## Running the Tests

**All tests:**
```bash
mvn test
```

**Smoke suite:**
```bash
mvn test -Dgroups=smoke
```

**Regression suite:**
```bash
mvn test -Dgroups=regression
```

Tests can also be executed directly via the TestNG configuration in IntelliJ IDEA.

### Test Artifacts

```text
target/
└── Screenshots/
```

---

## Key Concepts Demonstrated

- Selenium WebDriver & Java fundamentals
- Page Object Model (POM) architecture
- Data-driven testing with externalized JSON
- Explicit waits and synchronization strategy
- Dynamic XPath locator construction
- WebElement interaction and browser navigation
- URL and state validation
- Numeric extraction and calculation from UI text
- Soft assertions and structured test grouping/prioritization
- Screenshot capture on completion
- Custom WebDriver event listeners
- Git/GitHub-based version control workflow

---

## Roadmap

- [ ] Additional negative login scenarios
- [ ] Checkout field-validation coverage (required fields)
- [ ] Order-completion verification
- [ ] TestNG XML suite configuration
- [ ] Reusable, centralized wait utilities
- [ ] Parameterized cross-browser execution
- [ ] HTML test reporting
- [ ] CI/CD integration (GitHub Actions)
- [ ] Structured logging framework

---

## Author

**Tony Edwer**
Software Testing & QA Automation

`Java` · `Selenium` · `TestNG` · `Maven` · `Page Object Model` · `JSON` · `Data-Driven Testing` · `UI Automation`

---

## License

This project is intended for educational and portfolio purposes.
