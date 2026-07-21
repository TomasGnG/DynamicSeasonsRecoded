param(
    [Parameter(Mandatory=$true)]
    [string]$Version
)

if ($Version -notmatch "^\d+\.\d+\.\d+$") {
    Write-Host "Fehler: Version muss dem Format X.Y.Z entsprechen (z.B. 2.5.4)" -ForegroundColor Red
    exit 1
}

$tag = "v$Version"

# Version in build.gradle aktualisieren
$gradleFile = "build.gradle"
(Get-Content $gradleFile) -replace "version = '.*'", "version = '$Version'" | Set-Content $gradleFile
Write-Host "Version in $gradleFile auf $Version aktualisiert" -ForegroundColor Green

# Commit
git add $gradleFile
git commit -m "Bump version to $Version"

# Tag
git tag $tag

# Pushen
git push
git push origin $tag

Write-Host "Release $tag erstellt und gepusht!" -ForegroundColor Green
Write-Host "Der GitHub-Workflow baut jetzt automatisch und erstellt das Release." -ForegroundColor Cyan
