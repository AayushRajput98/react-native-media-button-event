
import { DeviceEventEmitter } from 'react-native';

class MediaButton {
    keyEventListener(cb) {
        this.removeKeyEventListener();
        this.listnerKeyEvent = DeviceEventEmitter.addListener('onMediaButtonEvent', cb);
      }
    
      removeKeyEventListener() {
        if (this.listnerKeyEvent) {
          this.listnerKeyEvent.remove();
          this.listnerKeyEvent = null;
        }
      }
}

export default new MediaButton;
