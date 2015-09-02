# General aliases
alias ll='ls -Altr'
alias nsid="/sbin/ip netns identify $$"

# Shortcuts for "swns" network namespace commands
alias swns="/sbin/ip netns exec swns"
alias swnssh="/sbin/ip netns exec swns /bin/bash --rcfile /etc/profile.d/ops-profile -i"
alias swnspids="/sbin/ip netns pids swns"

# Shortcuts for "nonet" network namespace commands
alias nonet="/sbin/ip netns exec nonet"
alias nonetsh="/sbin/ip netns exec nonet /bin/bash --rcfile /etc/profile.d/ops-profile -i"
alias nonetpids="/sbin/ip netns pids nonet"
