# Cyberpunk 2077 Wallpaper Scraper

## Description
This Java-based web scraper automates the process of downloading high-resolution wallpapers from the official [Cyberpunk 2077 website](https://www.cyberpunk.net/za/en/cyberpunk-2077#media). Using Selenium WebDriver and Java's HttpClient, the program navigates the site, retrieves available wallpapers, and downloads them to your local system.

## Prerequisites
Before running this scraper, ensure you have the following installed:

### Java Requirements
- **Minimum Java Version:** Java 11 or later
- **JDK Installation:** Install the latest Java Development Kit (JDK) appropriate for your system.
  - Download from [Oracle JDK](https://www.oracle.com/java/technologies/javase-downloads.html) or use OpenJDK from your package manager.
- **Java Runtime Environment (JRE):** Ensure the JRE is installed and properly configured.

### WebDriver Setup
Since this program utilizes Selenium WebDriver, ensure you have the necessary drivers installed and configured for your browser:

#### **Windows Setup**
1. **Google Chrome:** Download and install [ChromeDriver](https://chromedriver.chromium.org/downloads).
2. **Mozilla Firefox:** Download and install [GeckoDriver](https://github.com/mozilla/geckodriver/releases).
3. **Microsoft Edge:** Download and install [EdgeDriver](https://developer.microsoft.com/en-us/microsoft-edge/tools/webdriver/).

#### **MacOS Setup**
1. **Google Chrome:** Install ChromeDriver using Homebrew:
   ```sh
   brew install chromedriver
   ```
2. **Mozilla Firefox:** Install GeckoDriver:
   ```sh
   brew install geckodriver
   ```
3. **Safari:** Enable the built-in Safari WebDriver:
   ```sh
   sudo safaridriver --enable
   ```

## How to Use

### 1. Clone the Repository
```sh
git clone https://github.com/yourusername/Cyberpunk-Wallpaper-Scraper.git
cd Cyberpunk-Wallpaper-Scraper
```

### 2. Compile and Run
```sh
javac CyberpunkImageScraper.java
java CyberpunkImageScraper
```

### 3. Choosing a WebDriver
In the `CyberpunkImageScraper.java` file, uncomment the relevant WebDriver for your browser:
```java
// private static final WebDriver driver = new ChromeDriver();
// private static final WebDriver driver = new EdgeDriver();
// private static final WebDriver driver = new FirefoxDriver();
private static final WebDriver driver = new SafariDriver();
```

### 4. Downloaded Wallpapers
Wallpapers will be saved in:
```
~/Downloads/Cyberpunk_Wallpapers
```

## Notes
- **Performance Improvements Needed:** Future optimizations include removing `Thread.sleep()` and enhancing exception handling.
- **Ensure Drivers Are in PATH:** Make sure your WebDriver executables are accessible in the system `PATH`.
- **Use at Your Own Risk:** This scraper interacts with a live website, so changes to the website structure may break functionality.

---

### License
This project is open-source under the MIT License.

