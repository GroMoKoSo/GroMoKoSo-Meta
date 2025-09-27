#!/bin/bash

clone_or_pull() {
  local repo_url=$1
  local dir_name=$2

  echo "Trying to clone $dir_name..."
  
  if git clone "$repo_url" > /dev/null 2>&1; then
    echo "Success: Cloned $dir_name."
  else
    if [ -d "$dir_name" ]; then
      echo "Directory $dir_name already exists, trying to pull updates..."
      cd "$dir_name" || { echo "Failed to enter directory $dir_name, skipping."; return; }
      if git pull > /dev/null 2>&1; then
        echo "Pull successful for $dir_name."
      else
        echo "Pull failed for $dir_name, skipping."
      fi
      cd ..
    else
      echo "Failed to clone $dir_name and directory doesn't exist. Skipping."
    fi
  fi
}

clone_or_pull git@git.thm.de:softwarearchitektur-wz-ss24/studentswa2025/enton/userinterface.git userinterface
clone_or_pull git@git.thm.de:softwarearchitektur-wz-ss24/studentswa2025/enton/usermanagement.git usermanagement
clone_or_pull git@git.thm.de:softwarearchitektur-wz-ss24/studentswa2025/enton/apimanagement.git apimanagement
clone_or_pull git@git.thm.de:softwarearchitektur-wz-ss24/studentswa2025/enton/mcpmanagement.git mcpmanagement
clone_or_pull git@git.thm.de:softwarearchitektur-wz-ss24/studentswa2025/enton/spec2tool.git spec2tool

