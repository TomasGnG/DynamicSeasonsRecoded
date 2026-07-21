param(
    [Parameter(Mandatory=$true)]
    [string]$Version
)

if ($Version -notmatch "^\d+\.\d+\.\d+$") {
    Write-Host "Error: Version must follow the format X.Y.Z (e.g. 2.5.4)" -ForegroundColor Red
    exit 1
}

$tag = "v$Version"

# Version in build.gradle aktualisieren
$gradleFile = "build.gradle"
(Get-Content $gradleFile) -replace "version = '.*'", "version = '$Version'" | Set-Content $gradleFile
Write-Host "Updated version in $gradleFile to $Version" -ForegroundColor Green

# Commit
git add $gradleFile
git commit -m "Bump version to $Version"

# Tag
git tag $tag

# Pushen
git push
git push origin $tag

Write-Host "Release $tag created and pushed!" -ForegroundColor Green
Write-Host "The GitHub workflow will now build and create the release automatically." -ForegroundColor Cyan
