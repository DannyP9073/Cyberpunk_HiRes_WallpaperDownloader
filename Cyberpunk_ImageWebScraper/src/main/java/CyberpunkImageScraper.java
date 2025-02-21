import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class CyberpunkImageScraper {

    // TODO: remove thread.sleep to improve performance
    // TODO: add a wait function for page loading
    // TODO: improve exception handling performance

    //Uncomment needed driver for targeted browser
//    private static final WebDriver driver = new ChromeDriver();
//    private static final WebDriver driver = new EdgeDriver();
//    private static final WebDriver driver = new FirefoxDriver();
    private static final WebDriver driver = new SafariDriver();

    private static final Duration timeout = Duration.ofSeconds(5);
    private static final WebDriverWait wait = new WebDriverWait(driver, timeout);

    private static final String url = "https://www.cyberpunk.net/za/en/cyberpunk-2077#media";
    private static final String body_element = "body.p-cyberpunk.tingle-enabled";
    private static final String image_element = "div.tingle-modal.media-popup.tingle-modal--visible.tingle-modal--overflow";
    private static final String  image_box_element = "div.tingle-modal-box";
    private static final String image_content_element = "div.tingle-modal-box__content";
    private static final String media_popup_element = "div.media-popup__bottom";
    private static final String media_popup_content_element = "div.media-popup__resolutions";
    private static final String baseFolder = System.getProperty("user.home") + "/Downloads/Cyberpunk_Wallpapers";

    private static WebElement elementSection;

    public  static void main(String args[]) {

        AcceptCookie();
        NavigateToWallpaper();

        try {
            WebElement elementDiv2 = elementSection.findElement(By.cssSelector("div.l-gallery__swiper"));
            WebElement elementDiv3 = elementDiv2.findElement(By.cssSelector("div.swiper-wrapper"));
            WebElement elementDiv4 = elementDiv3.findElement(By.cssSelector("div.swiper-slide"));

            try {
                WebElement selectImage = wait.until(ExpectedConditions.elementToBeClickable(elementDiv4));
                selectImage.click();

            } catch (TimeoutException e) {
                System.out.println("Element was not clickable in time: " + e.getMessage());
            } catch (NoSuchElementException e) {
                System.out.println("Element not found: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Unexpected error occurred: " + e.getMessage());
            }

            WebElement elementBody = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(body_element)));
            WebElement element_image_view = elementBody.findElement(By.cssSelector(image_element));
            WebElement element_image_view_box = element_image_view.findElement(By.cssSelector(image_box_element));
            WebElement element_image_view_box_content = element_image_view_box.findElement(By.cssSelector(image_content_element));

            // Div class for listed images
            WebElement element_image_list_box = element_image_view_box_content.findElement(By.cssSelector("div.popup-swiper.swiper.swiper-fade.swiper-initialized.swiper-horizontal.swiper-pointer-events.swiper-watch-progress"));
            WebElement element_image_list_view = element_image_list_box.findElement(By.cssSelector("div.swiper-wrapper"));

            // Div class for navigation
            WebElement element_media_navigation_view = element_image_view_box_content.findElement(By.cssSelector("div.media-popup__navigation"));
            WebElement element_navigate_next = element_media_navigation_view.findElement(By.cssSelector("div.gallery-button-next"));

            int list_images_size = 0;

            if (element_image_list_view.isDisplayed()) {
                System.out.println("Modal gallery found!");
            }

            List<WebElement> list_images = element_image_list_view.findElements(By.cssSelector("div.swiper-slide"));
            list_images_size = list_images.size();

            System.out.println("Found " + "(" + list_images_size + ")" + " images!");

            int j = 0;
            GetImages(j);

            JavascriptExecutor js = (JavascriptExecutor) driver;
            for (int i = 1; i < list_images_size; i++) {
                try {

                    js.executeScript("arguments[0].click();", element_navigate_next);
                    System.out.println("Clicked next button using JavaScript: " + (i + 1));

                    j++;
                    GetImages(j);

                    Thread.sleep(500);

                } catch (Exception e) {
                    System.out.println("Failed to click next button using JavaScript on iteration " + (i + 1) + ": " + e.getMessage());
                }
            }
        } finally {
            driver.quit();
        }
    }

    private static void NavigateToWallpaper(){
        elementSection = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//section[@id='media']")));
        WebElement elementDiv1 = elementSection.findElement(By.cssSelector("div.l-gallery"));
        WebElement elementNav = elementDiv1.findElement(By.cssSelector("nav.l-gallery__type-nav"));
        WebElement elementUl = elementNav.findElement(By.tagName("ul"));

        try {
            List<WebElement> navItems = elementUl.findElements(By.tagName("li"));

            Thread.sleep(1000);
            for (WebElement item : navItems) {
                String dataType = item.getAttribute("data-type");
                if ("wallpaper".equals(dataType)) {

                    System.out.println("Found wallpaper tab!");

                    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", item);

                    wait.until(ExpectedConditions.elementToBeClickable(item)).click();

                    Thread.sleep(1000);
                    System.out.println("Wallpaper tab clicked.");
                    break;

                }
            }

        } catch (Exception e) {
            System.out.println("Failed to load list:" + e.getMessage());
        }
    }

    private static void AcceptCookie() {

        try {
            driver.get(url);
            Thread.sleep(3000);

            try {
                WebElement cookieButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("CybotCookiebotDialogBodyLevelButtonLevelOptinAllowAll")));
                cookieButton.click();
                System.out.println("Cookies accepted.");

            } catch (NoSuchElementException e) {
                System.out.println("No cookie popup found, continuing...");
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void GetImages(int folderNumber) {

        try {
            WebElement elementBody = driver.findElement(By.cssSelector(body_element));
            WebElement elementImageView = elementBody.findElement(By.cssSelector(image_element));
            WebElement elementImageViewBox = elementImageView.findElement(By.cssSelector(image_box_element));
            WebElement elementImageViewBoxContent = elementImageViewBox.findElement(By.cssSelector(image_content_element));
            WebElement elementBottomPopup = elementImageViewBoxContent.findElement(By.cssSelector(media_popup_element));
            WebElement elementResolutions = elementBottomPopup.findElement(By.cssSelector(media_popup_content_element));

            List<WebElement> imageLinks = elementResolutions.findElements(By.tagName("a"));
            List<String> imageUrls = new ArrayList<>();

            for (WebElement link : imageLinks) {
                String href = link.getAttribute("href");
                imageUrls.add(href);
                System.out.println("Found image URL: " + href);
            }

            DownloadImages(imageUrls, folderNumber);

        } catch (Exception e) {
            System.out.println("Error fetching image URLs: " + e.getMessage());
        }
    }

    private static void DownloadImages(List<String> urls, int folderNumber) {
        HttpClient httpClient = HttpClient.newHttpClient();
        File imageDir = new File(baseFolder + "/Set_" + folderNumber);
        if (!imageDir.exists()) {
            imageDir.mkdirs();
        }

        for (String imageUrl : urls) {
            try {
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(imageUrl))
                        .GET()
                        .build();

                HttpResponse<InputStream> response = httpClient.send(request, HttpResponse.BodyHandlers.ofInputStream());

                if (response.statusCode() == 200) {
                    String fileName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
                    Path filePath = Paths.get(imageDir.getAbsolutePath(), fileName);
                    filePath = getUniqueFilePath(filePath);

                    Files.copy(response.body(), filePath);
                    System.out.println("Downloaded: " + filePath.getFileName());
                } else {
                    System.out.println("Failed to download: " + imageUrl + " (HTTP " + response.statusCode() + ")");
                }

            } catch (Exception e) {
                System.out.println("Error downloading image: " + imageUrl);
                e.printStackTrace();
            }
        }
    }

    private static Path getUniqueFilePath(Path filePath) {
        int count = 1;
        String baseName = filePath.getFileName().toString();
        String name = baseName.contains(".") ? baseName.substring(0, baseName.lastIndexOf(".")) : baseName;
        String extension = baseName.contains(".") ? baseName.substring(baseName.lastIndexOf(".")) : "";

        while (Files.exists(filePath)) {
            filePath = Paths.get(filePath.getParent().toString(), name + "_" + count + extension);
            count++;
        }
        return filePath;
    }
}