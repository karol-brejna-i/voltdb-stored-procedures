
To use a proxy with Maven, you need either to configure the proxy settings in Maven's `settings.xml` file, or passing proxy information directly in the `mvn` command.


## Set up a proxy in the `settings.xml` file:

The file is located in the Maven **`conf`** directory (usually in `Maven_Home/conf/settings.xml`) or in the user-specific Maven directory (`~/.m2/settings.xml` on Unix-based systems or `C:\Users\<Your-Username>\.m2\settings.xml` on Windows).


1. Locate the `settings.xml` file:
   - System-wide: `{Maven_Home}/conf/settings.xml`
   - User-specific: `~/.m2/settings.xml` (create this file if it doesn't exist)

2. Add or update the `<proxies>` section in `settings.xml` with the following structure:

```xml
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0
                              https://maven.apache.org/xsd/settings-1.0.0.xsd">

    <proxies>
        <proxy>
            <id>example-proxy</id>
            <active>true</active>
            <protocol>http</protocol>
            <host>proxy.example.com</host>
            <port>8080</port>
            <username>proxyuser</username>
            <password>somepassword</password>
            <nonProxyHosts>www.google.com|*.example.com</nonProxyHosts> <!-- optional -->
        </proxy>
    </proxies>

</settings>
```

Explanation of the fields:

- **`<id>`**: A unique identifier for the proxy.
- **`<active>`**: If set to `true`, this proxy is active.
- **`<protocol>`**: The protocol used by the proxy (either `http` or `https`).
- **`<host>`**: The hostname of the proxy server.
- **`<port>`**: The port number of the proxy server.
- **`<username>` and `<password>`**: The credentials to authenticate with the proxy (if required).
- **`<nonProxyHosts>`**: A list of hosts that Maven should not use the proxy for, separated by `|`. You can use wildcards (e.g., `*.example.com`).


Once you've added this configuration to the `settings.xml` file, Maven will automatically route its HTTP or HTTPS requests through the proxy whenever necessary.


## Use `-D` properties in the `mvn` command

You can specify the proxy details using system properties in your Maven command:

```bash
mvn -DproxySet=true -DproxyHost=proxy.example.com -DproxyPort=8080 -DproxyUser=yourUser -DproxyPassword=yourPassword clean install
```

This is a one-time proxy setting for the particular Maven command you are running, so you'll need to use the proxy properties every time you run Maven with a proxy.


### "Dynamic alias" for `mvn`
You can create a "dynamic alias" in your .bashrc (or .bash_profile for some systems) that will check if the HTTP_PROXY variable is set, and modify the mvn command accordingly.


```bash
mvn() {
  if [[ -n "$HTTP_PROXY" ]]; then
    # Extract the proxy host and port from the HTTP_PROXY variable
    proxy_host=$(echo "$HTTP_PROXY" | sed -e 's|http://||' -e 's|https://||' -e 's|:.*||')
    proxy_port=$(echo "$HTTP_PROXY" | sed -n 's|.*:\([0-9]*\).*|\1|p')

    # Run mvn with proxy settings
    if [[ -n "$proxy_port" ]]; then
      command mvn -DproxySet=true -DproxyHost="$proxy_host" -DproxyPort="$proxy_port" "$@"
    else
      command mvn -DproxySet=true -DproxyHost="$proxy_host" "$@"
    fi
  else
    # Run mvn normally if no HTTP_PROXY is set
    command mvn "$@"
  fi
}
```

    Remeber to reload your .bashrc file (i.e. `source ~/.bashrc`).
