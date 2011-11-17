        .data
        .globl  LF
LF:     .fill   4                       # int LF;
        .text
        .globl  gcd                     
gcd:    pushl   %ebp                    # Start function gcd
        movl    %esp,%ebp               
.L0001:                                 # Start while-statement
        movl    8(%ebp),%eax            # a
        pushl   %eax                    
        movl    12(%ebp),%eax           # b
        popl    %ecx                    
        cmpl    %eax,%ecx               
        setne   %al                     # Test !=
        movzbl  %al,%eax                
        cmpl    $0,%eax                 
        je      .L0002                  
                                        # Start if-statement
        movl    8(%ebp),%eax            # a
        pushl   %eax                    
        movl    12(%ebp),%eax           # b
        popl    %ecx                    
        cmpl    %eax,%ecx               
        setl    %al                     # Test <
        movzbl  %al,%eax                
        cmpl    $0,%eax                 
        je      .L0004                  
        movl    12(%ebp),%eax           # b
        pushl   %eax                    
        movl    8(%ebp),%eax            # a
        movl    %eax,%ecx               
        popl    %eax                    
        subl    %ecx,%eax               # Compute -
        movl    %eax,12(%ebp)           # b =
        jmp     .L0003                  
.L0004:                                 #   else-part
        movl    8(%ebp),%eax            # a
        pushl   %eax                    
        movl    12(%ebp),%eax           # b
        movl    %eax,%ecx               
        popl    %eax                    
        subl    %ecx,%eax               # Compute -
        movl    %eax,8(%ebp)            # a =
.L0003:                                 # End if-statement
        jmp     .L0001                  
.L0002:                                 # End while-statement
        movl    8(%ebp),%eax            # a
        jmp     .exit$gcd               # return-statement
.exit$gcd:                                
        popl    %ebp                    
        ret                             # End function gcd
        .globl  main                    
main:   pushl   %ebp                    # Start function main
        movl    %esp,%ebp               
        subl    $8,%esp                 # Get 8 bytes local data space
        movl    $10,%eax                # 10
        movl    %eax,LF                 # LF =
        movl    $63,%eax                # 63
        pushl   %eax                    # Push parameter #1
        call    putchar                 # Call putchar
        popl    %ecx                    # Pop parameter #1
        movl    $32,%eax                # 32
        pushl   %eax                    # Push parameter #1
        call    putchar                 # Call putchar
        popl    %ecx                    # Pop parameter #1
        call    getint                  # Call getint
        movl    %eax,-4(%ebp)           # v1 =
        call    getint                  # Call getint
        movl    %eax,-8(%ebp)           # v2 =
        movl    -8(%ebp),%eax           # v2
        pushl   %eax                    # Push parameter #2
        movl    -4(%ebp),%eax           # v1
        pushl   %eax                    # Push parameter #1
        call    gcd                     # Call gcd
        popl    %ecx                    # Pop parameter #1
        popl    %ecx                    # Pop parameter #2
        pushl   %eax                    # Push parameter #1
        call    putint                  # Call putint
        popl    %ecx                    # Pop parameter #1
        movl    LF,%eax                 # LF
        pushl   %eax                    # Push parameter #1
        call    putchar                 # Call putchar
        popl    %ecx                    # Pop parameter #1
        movl    $0,%eax                 # 0
        pushl   %eax                    # Push parameter #1
        call    exit                    # Call exit
        popl    %ecx                    # Pop parameter #1
.exit$main:
        addl    $8,%esp                 # Release local data space
        popl    %ebp                    
        ret                             # End function main
