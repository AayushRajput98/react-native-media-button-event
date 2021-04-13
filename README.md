
# react-native-media-button-handling

## Getting started

`$ npm install react-native-media-button-handling --save`

### Mostly automatic installation

`$ react-native link react-native-media-button-handling`

### Manual installation


#### Android

1. Open up `android/app/src/main/java/[...]/MainActivity.java`
  - Add `import com.mediabuttonhandling.reactnative.RNMediaButtonHandlingPackage;` to the imports at the top of the file
  - Add `new RNMediaButtonHandlingPackage()` to the list returned by the `getPackages()` method
2. Append the following lines to `android/settings.gradle`:
  	```
  	include ':react-native-media-button-handling'
  	project(':react-native-media-button-handling').projectDir = new File(rootProject.projectDir, 	'../node_modules/react-native-media-button-handling/android')
  	```
3. Insert the following lines inside the dependencies block in `android/app/build.gradle`:
  	```
      compile project(':react-native-media-button-handling')
  	```


## Usage
```javascript
import RNMediaButtonHandling from 'react-native-media-button-handling';

// TODO: What to do with the module?
RNMediaButtonHandling;
```
  