import { registerPlugin } from '@capacitor/core';
import type { DeviceIntegrityPlugin } from './definitions';

export const DeviceIntegrity = registerPlugin<DeviceIntegrityPlugin>(
  'DeviceIntegrity'
);

