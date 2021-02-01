$gitSrc = "C:\Users\hello\Documents\Metropolia Projects\assembly-robot-simulator"
$svnMirror = "D:\SVN\Metropolia\assembly-robot-simulator\trunk"

# Change workdir to Git repo

cd $gitSrc

# Pull latest revision data
$rev = git rev-parse --short HEAD
$timestamp = Get-Date -Format "dd.MM.yyyy, HH:mm"

echo "Mirroring HEAD revision $rev from Git repository at $gitSrc to SVN repository at $svnMirror."

# Mirror files

echo "Copying files..."
cp -Path "$gitSrc\*" -Destination $svnMirror -Exclude @(Get-Content "$gitSrc\.svnignore") -Recurse -Force

# Commit

echo "Uploading rev. $rev from Git to SVN."

svn cleanup $svnMirror
svn add --force "$svnMirror\*"
svn commit $svnMirror -m "Uploaded rev. $rev from mirror at $timestamp"