import { WebPlugin } from '@capacitor/core';
import type {
  DeviceIntegrityPlugin,
  DeviceIntegrityResult
} from './definitions';

export class DeviceIntegrityWeb
  extends WebPlugin
  implements DeviceIntegrityPlugin {

  async checkIntegrity():
    Promise<DeviceIntegrityResult> {

    return {
      isRooted: false,
      isEmulator: false,
      isFridaDetected: false,

      rootScore: 0,
      emulatorScore: 0,
      fridaScore: 0,

      rootReasons: [],
      emulatorReasons: [],
      fridaReasons: []
    };
  }
}