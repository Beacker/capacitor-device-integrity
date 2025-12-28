# capacitor-device-integrity

plugin detect emulator

## Install

```bash
npm install capacitor-device-integrity
npx cap sync
```

## API

<docgen-index>

* [`detectEmulator()`](#detectemulator)
* [Interfaces](#interfaces)

</docgen-index>

<docgen-api>
<!--Update the source file JSDoc comments and rerun docgen to update the docs below-->

### detectEmulator()

```typescript
detectEmulator() => Promise<EmulatorDetectionResult>
```

**Returns:** <code>Promise&lt;<a href="#emulatordetectionresult">EmulatorDetectionResult</a>&gt;</code>

--------------------


### Interfaces


#### EmulatorDetectionResult

| Prop             | Type                  |
| ---------------- | --------------------- |
| **`isEmulator`** | <code>boolean</code>  |
| **`reasons`**    | <code>string[]</code> |

</docgen-api>
