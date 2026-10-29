'use strict';

const test = require('node:test');
const assert = require('node:assert/strict');
const { isWindowsCounterDiagnostic } = require('./runtime_log');

const detail = 'Unable to locate English counter names in registry Perflib 009. Counters may need to be rebuilt: ';
const line = '[15:03:33] [main/ERROR] [oshi.driver.windows.registry.HkeyPerformanceDataUtil/]: ' + detail;

test('recognises the exact Windows counter diagnostic in both logger formats', () => {
    assert.equal(isWindowsCounterDiagnostic(line, 'win32'), true);
    assert.equal(isWindowsCounterDiagnostic(line.replace('oshi.driver.windows.registry', 'os.dr.wi.re'), 'win32'), true);
});

test('does not allow it on another operating system', () => {
    assert.equal(isWindowsCounterDiagnostic(line, 'linux'), false);
});

test('does not hide a fatal message or another logger', () => {
    assert.equal(isWindowsCounterDiagnostic(line.replace('/ERROR]', '/FATAL]'), 'win32'), false);
    assert.equal(isWindowsCounterDiagnostic(line.replace('HkeyPerformanceDataUtil', 'BaseMetals'), 'win32'), false);
});

test('does not hide a different counter failure', () => {
    assert.equal(isWindowsCounterDiagnostic(line.replace(detail, 'Could not read system counters'), 'win32'), false);
});
