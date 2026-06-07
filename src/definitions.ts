export interface DeviceIntegrityResult {
  isRooted: boolean;
  isEmulator: boolean;
  isFridaDetected: boolean;

  rootScore: number;
  emulatorScore: number;
  fridaScore: number;

  rootReasons: string[];
  emulatorReasons: string[];
  fridaReasons: string[];
}

export interface DeviceIntegrityPlugin {
  checkIntegrity(): Promise<DeviceIntegrityResult>;
}