export interface DeviceIntegrityPlugin {
  echo(options: { value: string }): Promise<{ value: string }>;
}
