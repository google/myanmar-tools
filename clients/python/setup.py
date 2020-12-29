import setuptools

with open('README.rst') as f:
    long_description = f.read()

setuptools.setup(
    name='myanmartools',
    version='1.2.1',
    url='https://github.com/google/myanmar-tools',
    author='William (Wai Yan) Zhu',
    author_email='williamzhu345@gmail.com',
    classifiers=[
        'Development Status :: 4 - Beta',
        'Intended Audience :: Developers',
        'Intended Audience :: Science/Research',
        'License :: OSI Approved :: Apache Software License',
        'Operating System :: OS Independent',
        'Programming Language :: Python :: 3.8',
        'Topic :: Text Processing'
    ],
    license='Apache License, Version 2.0',
    description='Tools for processing font encodings used in Myanmar',
    long_description=long_description,
    long_description_content_type='text/x-rst',
    keywords=['burmese', 'encoding', 'myanmar', 'nlp', 'unicode', 'zawgyi'],
    platforms=['any'],
    packages=setuptools.find_packages('src'),
    package_dir={'': 'src'},
    include_package_data=True,
    package_data={'myanmartools': ['resources/*']},
    python_requires='>=3.8'
)
