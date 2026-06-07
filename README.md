# capacitor-device-integrity

plugin detect emulator

## Install

```bash
npm install capacitor-device-integrity
npx cap sync
```

## API

<docgen-index>

* [`checkIntegrity()`](#checkintegrity)
* [Interfaces](#interfaces)

</docgen-index>

<docgen-api>
<!--Update the source file JSDoc comments and rerun docgen to update the docs below-->

### checkIntegrity()

```typescript
checkIntegrity() => Promise<DeviceIntegrityResult>
```

**Returns:** <code>Promise&lt;<a href="#deviceintegrityresult">DeviceIntegrityResult</a>&gt;</code>

--------------------


### Interfaces


#### DeviceIntegrityResult

| Prop                  | Type                  |
| --------------------- | --------------------- |
| **`isRooted`**        | <code>boolean</code>  |
| **`isEmulator`**      | <code>boolean</code>  |
| **`isFridaDetected`** | <code>boolean</code>  |
| **`rootScore`**       | <code>number</code>   |
| **`emulatorScore`**   | <code>number</code>   |
| **`fridaScore`**      | <code>number</code>   |
| **`rootReasons`**     | <code>string[]</code> |
| **`emulatorReasons`** | <code>string[]</code> |
| **`fridaReasons`**    | <code>string[]</code> |

</docgen-api>
