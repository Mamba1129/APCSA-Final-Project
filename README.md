APCSA-Final-Project

This is a backup of your project. It contains Java source files and a small Maven pom.

Quick actions to push to GitHub (recommended: SSH):

1) Add SSH key to GitHub
   - Generate key if you haven't: `ssh-keygen -t ed25519 -C "your-email@example.com"` (press Enter to accept defaults)
   - Start agent and add key:
     ```powershell
     Start-Service ssh-agent
     Set-Service -Name ssh-agent -StartupType Automatic
     ssh-add $env:USERPROFILE\.ssh\id_ed25519
     ```
   - Copy public key and add to GitHub: `Get-Content $env:USERPROFILE\.ssh\id_ed25519.pub | Set-Clipboard`
   - GitHub → Settings → SSH and GPG keys → New SSH key → paste key.

2) Verify SSH works:
   ```powershell
   ssh -T git@github.com
   ```

3) Switch repo remote to SSH and push:
   ```powershell
   cd "c:\Users\bestv\OneDrive\Desktop\Documents\AP_comscie"
   git remote remove origin
   git remote add origin git@github.com:Mamba1129/APCSA-Final-Project.git
   git branch -M main
   git push -u origin main
   ```

Alternative: use `gh auth login` (GitHub CLI) to authenticate over HTTPS.

Security: revoke any exposed personal access tokens immediately: GitHub → Settings → Developer settings → Personal access tokens.

If you want, tell me "push now" after you've added the SSH key and verified it, and I'll push for you.
