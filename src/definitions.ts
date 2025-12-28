export interface EmulatorDetectionResult {
  isEmulator: boolean;
  reasons: string[];
}

export interface DeviceIntegrityPlugin {
  detectEmulator(): Promise<EmulatorDetectionResult>;
}
