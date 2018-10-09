
### It is a simple library, which will inform when crash arrives.

---

### When crash Arrives

![Crash Report](https://raw.githubusercontent.com/Collabalist/CrashLet/master/app/src/screen_1.png)
## Implementation
In your **build.gradle**

````js
    allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
````

In your application's **build.gradle**
    
````groovy
    dependencies {
	        implementation 'com.github.Collabalist:CrashLet:1.5'
	}
````

## Usage
In your application's **Application class**
````java
    @Override
    public void onCreate() {
        super.onCreate();
        CrashLet.with(this)
            .addRecipient("abc@gmail.com")
            .addRecipient("xyz@gmail.com")
            .showStackTrace(true)
            .init();
    }
````
