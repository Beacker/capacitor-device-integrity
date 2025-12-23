import { WebPlugin } from '@capacitor/core';

import type { DeviceIntegrityPlugin } from './definitions';

export class DeviceIntegrityWeb extends WebPlugin implements DeviceIntegrityPlugin {
  async echo(options: { value: string }): Promise<{ value: string }> {
    console.log('ECHO', options);
    return options;
  }
}
