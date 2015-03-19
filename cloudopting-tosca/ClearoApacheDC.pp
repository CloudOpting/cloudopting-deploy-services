node 'ClearoApacheDC.csi.local' {

class { 'apache':
      
      
      
    }

apache::vhost { 'www.clearo.com':
      
      
      aliases => [
  { aliasmatch       => '^/image/(.*)\.jpg$',
    path             => '/files/jpg.images/$1.jpg',
  },
  { alias            => '/image',
    path             => '/ftp/pub/image',
  },
  { scriptaliasmatch => '^/cgi-bin(.*)',
    path             => '/usr/local/share/cgi-bin$1',
  },
  { scriptalias      => '/nagios/cgi-bin/',
    path             => '/usr/lib/nagios/cgi-bin/',
  },
  { alias            => '/nagios',
    path             => '/usr/share/nagios/html',
  },
],
      directories => [
        { path        => '/path/to/directory',
          addhandlers => [{ handler => 'cgi-script', extensions => ['.cgi']}],
        },
      ],
      
      options => ['Indexes','FollowSymLinks','MultiViews'],
      proxy_pass => [
    { 'path' => '/a', 'url' => 'http://backend-a/' },
    { 'path' => '/b', 'url' => 'http://backend-b/' },
    { 'path' => '/c', 'url' => 'http://backend-a/c', 'params' => {'max'=>20, 'ttl'=>120, 'retry'=>300}},
    { 'path' => '/l', 'url' => 'http://backend-xy',
      'reverse_urls' => ['http://backend-x', 'http://backend-y'] },
    { 'path' => '/d', 'url' => 'http://backend-a/d',
      'params' => { 'retry' => '0', 'timeout' => '5' }, },
    { 'path' => '/e', 'url' => 'http://backend-a/e',
      'keywords' => ['nocanon', 'interpolate'] },
  ],
      redirect_source => ['/images','/downloads'],
      redirect_dest => ['http://img.example.com/','http://downloads.example.com/'],
      redirect_status => ['temp','permanent'],
      
}
apache::vhost { 'www.trasparenza.it':
      
      
      
      
      
      
      
      
      
      
      
}

}