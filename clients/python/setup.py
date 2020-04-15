import setuptools

with open('README.rst') as f:
    long_description = f.read()

setuptools.setup(name='myanmartools',
    version='0.1',
    author='William (Wai Yan) Zhu',
    author_email='williamzhu345@gmail.com',
    description='Tools for processing font encodings used in Myanmar',
    long_description=long_description,
    long_description_content_type='text/x-rst',
    url='https://github.com/google/myanmar-tools',
    packages=setuptools.find_packages('src'),
    package_dir={'': 'src'},
    include_package_data=True,
    package_data={'myanmartools': ['resources/*']},
    install_requires=['numpy>=1.18'],
    python_requires='>=3.6')
