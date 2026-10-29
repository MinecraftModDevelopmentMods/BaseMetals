'use strict';

// Minecraft's system probe may report missing Windows performance-counter names.
function isWindowsCounterDiagnostic(line, platform = process.platform) {
    return platform === 'win32'
        && /\/ERROR\] \[(?:oshi\.driver\.windows\.registry\.HkeyPerformanceDataUtil|os\.dr\.wi\.re\.HkeyPerformanceDataUtil)\/\]: Unable to locate English counter names in registry Perflib 009\. Counters may need to be rebuilt: $/.test(line);
}

module.exports = { isWindowsCounterDiagnostic };
