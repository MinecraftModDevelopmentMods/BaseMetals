param(
    [Parameter(Mandatory = $true)][string]$CandidateJar,
    [Parameter(Mandatory = $true)][string]$Repository
)

$ErrorActionPreference = 'Stop'
$candidateVersion = '4.1.0.116051'
$candidateSha256 = 'CDF59D83F191C5FBCD8921228DB577066A63A8FEB25CA79DB4EAEEA32D32DB04'

if ((Get-FileHash -LiteralPath $CandidateJar -Algorithm SHA256).Hash -ne $candidateSha256) {
    throw 'The OreSpawn candidate differs from the qualified build.'
}

Add-Type -AssemblyName System.IO.Compression.FileSystem
$archive = [IO.Compression.ZipFile]::OpenRead((Resolve-Path -LiteralPath $CandidateJar).Path)
try {
    $metadata = $archive.GetEntry('META-INF/mods.toml')
    if ($null -eq $metadata) { throw 'OreSpawn mod metadata is missing.' }
    $reader = [IO.StreamReader]::new($metadata.Open())
    try { $text = $reader.ReadToEnd() } finally { $reader.Dispose() }
    if (!$text.Contains('version="4.1.0.116051"') -or !$text.Contains('versionRange="[1.16.5,1.17)"') -or
            $null -eq $archive.GetEntry('zone/moddev/mc/orespawn/api/client/WorldSettingsExtensionRegistry.class')) {
        throw 'Expected the OreSpawn 1.16.5 candidate with its configuration-screen API.'
    }
} finally { $archive.Dispose() }

$artifactDirectory = Join-Path $Repository "zone/moddev/mc/orespawn/OreSpawn/$candidateVersion"
New-Item -ItemType Directory -Path $artifactDirectory -Force | Out-Null
Copy-Item -LiteralPath $CandidateJar -Destination (Join-Path $artifactDirectory "OreSpawn-$candidateVersion.jar")
$pom = @"
<project xmlns="http://maven.apache.org/POM/4.0.0">
  <modelVersion>4.0.0</modelVersion>
  <groupId>zone.moddev.mc.orespawn</groupId>
  <artifactId>OreSpawn</artifactId>
  <version>$candidateVersion</version>
</project>
"@
[IO.File]::WriteAllText((Join-Path $artifactDirectory "OreSpawn-$candidateVersion.pom"),
        $pom.Replace("`r`n", "`n") + "`n", [Text.UTF8Encoding]::new($false))
Write-Output "Staged verified OreSpawn $candidateVersion in $Repository"
