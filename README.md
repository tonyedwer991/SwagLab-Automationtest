# SwagLab-Automationtest
# Swag Labs Automation Testing Framework

Automated test suite for [Swag Labs](https://www.saucedemo.com/) built with **Selenium WebDriver**, **TestNG**, and **Java**, following the **Page Object Model (POM)** design pattern with **JSON-based Data-Driven Testing**.

## Overview

This framework covers the core login flow and post-login inventory page validation for Swag Labs. All test data (valid/invalid credentials) is externalized to a JSON file rather than hardcoded, and all element interactions use explicit waits — no `Thread.sleep()` anywhere in the codebase.

## Tech Stack

| Tool | Purpose |
|---|---|
| Java 17 | Language |
| Selenium WebDriver 4.39.0 | Browser automation |
| TestNG 7.10.2 | Test runner, annotations, assertions |
| Maven | Dependency management & build |
| org.json | JSON parsing for data-driven testing |

## Project Structure

```
SwagLabautomationtest/
├── pom.xml
├── testng.xml
├── testData/
│   └── testData.json          # Valid / invalid / no-password credential sets
└── src/test/java/
    ├── BASE/
    │   └── Basetests.java      # Browser setup & teardown (@BeforeMethod / @AfterMethod)
    ├── PAGES/
    │   ├── Loginpage.java      # Login page locators & actions
    │   └── InventroryPage.java # Inventory page locators & actions
    ├── utils/
    │   └── DataDriven.java     # jsonReader() — reads test data from testData.json
    └── tests/
        ├── Logintest.java      # Login test scenarios
        └── Inventorytest.java  # Inventory page test scenarios
```

## Design Pattern: Page Object Model (POM)

- **Page classes** (`PAGES/`) hold locators and low-level element interactions only.
- **Test classes** (`tests/`) orchestrate calls to page objects and contain all assertions.
- **`Basetests`** centralizes WebDriver lifecycle so every test class gets a fresh browser session automatically.
- **`DataDriven`** decouples test data from test logic — page objects and test methods never contain hardcoded credentials.

## Test Scenarios

| # | Scenario | Class | Verifies |
|---|---|---|---|
| 1 | Successful login | `Logintest.validlogintest` | URL redirects to `/inventory.html` |
| 2 | Invalid login | `Logintest.invalidlogintest` | Error message: *"Username and password do not match any user in this service"* |
| 3 | Login without password | `Logintest.logintestwithoutpassword` | Error message: *"Password is required"* |
| 4 | Inventory page elements | `Inventorytest.InventoryTest` | Page title = "Swag Labs", cart icon visible, 6 products displayed |

## Prerequisites

- JDK 17+
- Maven (bundled with IntelliJ IDEA)
- Google Chrome (Selenium Manager, bundled with Selenium 4.6+, auto-resolves the matching driver — no manual chromedriver setup needed)

## Getting Started

```bash
# Clone the repository
git clone https://github.com/<your-username>/SwagLabautomationtest.git
cd SwagLabautomationtest
```

Open the folder in IntelliJ IDEA (`File → Open`) and let Maven import dependencies automatically.

## Running the Tests

**Via testng.xml (IntelliJ):**
Right-click `testng.xml` → `Run 'testng.xml'`

**Via Maven:**
```bash
mvn clean test
```

**Individual test class:**
Right-click `Logintest.java` or `Inventorytest.java` → `Run`

## Test Data

Credentials live in `testData/testData.json` and are never hardcoded in test methods:

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
    "username": "error_user"
  }
}
```

## Key Practices Followed

- ✅ Page Object Model — locators and actions separated from test logic
- ✅ Data-Driven Testing — all credentials sourced from JSON via `DataDriven.jsonReader()`
- ✅ Explicit waits (`WebDriverWait` + `ExpectedConditions`) — no `Thread.sleep()`
- ✅ TestNG annotations for setup/teardown (`@BeforeMethod`, `@AfterMethod`) and grouping (`groups = "regression"` / `"smoke"`)
- ✅ Assertions on every test to validate expected outcomes

## Notes

`DataDriven.jsonReader()` resolves `testData.json` via `System.getProperty("user.dir") + "/testData/testData.json"`, which relies on the working directory matching the project root (true by default in IntelliJ's run configuration). If running from CI or a different working directory, consider moving `testData.json` into `src/test/resources` and switching to a classpath-based reader (`getClassLoader().getResourceAsStream(...)`) for a more portable setup.

## Author

Built as a learning project to practice Selenium + TestNG automation using POM and data-driven testing principles.
